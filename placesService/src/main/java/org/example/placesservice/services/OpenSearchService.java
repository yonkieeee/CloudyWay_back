package org.example.placesservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.placesservice.dto.PlaceDTO;
import org.example.placesservice.mappers.PlaceMapper;
import org.example.placesservice.models.PlaceModel;
import org.example.placesservice.services.impl.PlaceServiceImpl;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.query.FuzzyQueryBuilder;
import org.opensearch.index.query.MatchAllQueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.index.reindex.BulkByScrollResponse;
import org.opensearch.index.reindex.DeleteByQueryRequest;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenSearchService {

    private static final String INDEX = "places";
    private static final Logger logger = LoggerFactory.getLogger(OpenSearchService.class);
    private final ObjectMapper objectMapper;
    private final PlaceServiceImpl placeService;
    private final RestHighLevelClient client;
    private final PlaceMapper placeMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void indexAllPlacesOnStart() {
        try {
            SearchRequest searchRequest = new SearchRequest(INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchRequest.source(searchSourceBuilder);
            searchRequest.source().size(1);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            //DeleteByQueryRequest request = new DeleteByQueryRequest(INDEX);
            //MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
            //request.setQuery(matchAllQuery);
            //BulkByScrollResponse bulkScrollResponse = client.deleteByQuery(request, RequestOptions.DEFAULT);

            if (searchResponse.getHits().getTotalHits().value == 0) {
                List<PlaceDTO> places = placeService.getAllPlaces();
                BulkRequest bulkRequest = new BulkRequest();
                for (PlaceDTO place : places) {
                    IndexRequest indexRequest = new IndexRequest(INDEX)
                            .source(objectMapper.convertValue(place, Map.class), XContentType.JSON);
                    bulkRequest.add(indexRequest);
                }
                if (bulkRequest.numberOfActions() > 0) {
                    BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                    if (bulkResponse.hasFailures()) {
                        logger.error(bulkResponse.buildFailureMessage());
                    } else {
                        logger.info("Successfully indexed places");
                    }
                } else {
                    logger.info("There no places to index in db");
                }
            } else {
                logger.info("Index is not empty, indexing is not allowed");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public List<Map<String, Object>> fuzzySearchByPlaceName(String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        FuzzyQueryBuilder fuzzyQuery = QueryBuilders.fuzzyQuery("placeName", query);
        searchSourceBuilder.query(fuzzyQuery);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return getSearchResults(searchResponse);
    }

    private List<Map<String, Object>> getSearchResults(SearchResponse searchResponse) {
        List<Map<String, Object>> searchHits = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            searchHits.add(hit.getSourceAsMap());
        }
        return searchHits;
    }

    public void addPlacesToIndex(PlaceModel place) {
        IndexRequest request = new IndexRequest(INDEX)
                .source(objectMapper.convertValue(placeMapper.toDTO(place), Map.class), XContentType.JSON);
        try {
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
            logger.info("Place indexed to OpenSearch");
        } catch (IOException e) {
            logger.error("Cannot index place");
        }
    }
}
