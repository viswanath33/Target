package com.target;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.index.query.QueryBuilders;

//Rest call to load and read data to/from Elastic search
@RestController
@RequestMapping("/es")
public class ESWriter { 

	@RequestMapping("method = RequestMethod.POST")	// URl will be  like /es/?key=mykey&indexname=myindexname&documenttype=mydocumenttype&documentID=1
	public void loadToElastic(@RequestParam(value="key", defaultValue="default") String key,
			@RequestParam(value="indexname", defaultValue="indexname") String indexname,
			@RequestParam(value="documenttype", defaultValue="documenttype") String documenttype,
			@RequestParam(value="documentID", defaultValue="1") String documentID)
	{
		Client client = null;
		try
		{
			client = ElasticUtil.getClient();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put(key,key+" : Value");

			//loads map in Elastic
			client.prepareIndex(indexname,documenttype,documentID).setSource(map).execute().actionGet();

		}
		catch(Exception e)
		{
			System.out.println("Exception occured : "+e.getMessage());
		}
		finally{
			if(null != client)
				ElasticUtil.close(client);
		}
	}

	//reading from Elastic  
	@RequestMapping("method = RequestMethod.GET")	// URl will be  like /es/?type=documenttype&index=myindexname&field=key&value=keyValue
	public static void searchDocument(Client client, String index, String type,
			String field, String value){
		SearchResponse response = client.prepareSearch(index)
				.setTypes(type)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(QueryBuilders.fieldQuery(field, value))
				.setExplain(true)
				.execute()
				.actionGet();
		SearchHit[] results = response.getHits().getHits();
		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String,Object> result = hit.getSource();   
			System.out.println(result);
		}
	} 
}