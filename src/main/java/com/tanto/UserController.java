package com.tanto;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.hadoop.rest.HttpStatus;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.elasticsearch.search.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/users")

public class UserController {
	@Autowired
	Client client;
	
	@PostMapping("/tambah")
	public String create(@RequestBody User user) throws IOException {
		IndexResponse response = client.prepareIndex("users","_doc",user.getIdUser())
				.setSource(jsonBuilder()
						.startObject()
						.field("id_user", user.getIdUser())
						.field("name", user.getName())
						.field("nickname", user.getNickname())
						.field("hobby", user.getHobby())
						.field("date", user.getOncreat())
						.field("address", user.getAddress())
						.endObject()
						)
				.get();
		System.out.println("respond id " +response.getId());
		return response.getResult().toString();
	}
	
	@GetMapping("/lihat")
	public Map<String, Object> views(){
		Map<String, Object> data = null;
		SearchResponse response = client.prepareSearch("users")
				.setTypes("_doc")
				.setSearchType(SearchType.QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.matchAllQuery())
				.get();
		
		
		List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
		return searchHits.get(0).getSourceAsMap();
	}
	
	
	@GetMapping("/lihat/{id}")
	public Map<String, Object> view(@PathVariable final String id){
		GetResponse getResponse = client.prepareGet("users", "_doc", id).get();
		System.out.println(getResponse.getSource());
		
		Map<String, Object> error = new HashMap<String, Object>();
		error.put("message", "Data tidak ditemukan");
		
		if(getResponse.getSource() == null) {
			return error;
		}else {
			return getResponse.getSource();
		}
	}
	
	@GetMapping("/search/nama/{nama}")
	public Map<String, Object> searchByName(@PathVariable final String nama){
		Map<String, Object> data = null;
		SearchResponse response = client.prepareSearch("users")
				.setTypes("_doc")
				.setSearchType(SearchType.QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.matchQuery("name", nama))
				.get();
		List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
		
		System.out.println(searchHits.get(0));
		data = searchHits.get(0).getSourceAsMap();
		return data;	
	}
	
	@PostMapping("/ubah/{id}")
	public String update(@PathVariable final String id, @RequestBody User user) throws IOException {
		UpdateRequest updateReq = new UpdateRequest();
		updateReq.index("users")
		.type("_doc")
		.id(id)
		.doc(jsonBuilder()
				.startObject()
				.field("name", user.getName())
				.field("nickname", user.getNickname())
				.field("hobby", user.getHobby())
				.field("address", user.getAddress())
				.endObject());
		
		try {
			UpdateResponse updateRes = client.update(updateReq).get();
			System.out.println(updateRes.status());
			return updateRes.status().toString();
		}catch (InterruptedException | ExecutionException e) {
			System.out.println(e);
		}
		
		return "Exception";
	}
	
	@GetMapping("/hapus/{id}")
	public String delete(@PathVariable final String id) {
		DeleteResponse deleteRes = client.prepareDelete("users", "_doc", id).get();
		
		System.out.println(deleteRes.getResult().toString());
		return deleteRes.getResult().toString();
	}
}
