package tech.knowshipp.vaccinebooking.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tech.knowshipp.vaccinebooking.dtos.Center;
import tech.knowshipp.vaccinebooking.dtos.CowinResponse;
import tech.knowshipp.vaccinebooking.dtos.Session;

public class SlotFinder {
	
	private static final Logger log = LoggerFactory.getLogger(SlotFinder.class);
	
	private String finderUrl;
	private HttpHeaders headers;
	private final BlockingQueue<String> pincodeQueue;
	private RestTemplate rest;
	private final MusicService musicService;

	public SlotFinder(BlockingQueue<String> pincodeQueue, MusicService musicService) {
		this.pincodeQueue = pincodeQueue;
		this.musicService = musicService;
		headers = new HttpHeaders();
		headers.add("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:79.0) Gecko/20100101 Firefox/79.0");
		headers.add("Accept", "application/json, text/plain, */*");
		headers.add("Accept-Language", "en-US,en;q=0.5");
		headers.add("Origin", "https://www.cowin.gov.in");
		headers.add("Connection", "keep-alive");
		headers.add("Referer", "https://www.cowin.gov.in/home");
		headers.add("TE", "Trailers");
		finderUrl = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin";
		rest = new RestTemplate();
	}

	/**
	 * Consume item from the queue.
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 */
	public void findSlotsConsumer() throws InterruptedException, JsonMappingException, JsonProcessingException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		String json = "{\"centers\":[{\"center_id\":605834,\"name\":\"UPHC Surajpur\",\"address\":\"UPHC SURAJPUR\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"fe2763b2-114b-4d93-9758-2f13a4b8f910\",\"date\":\"09-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVAXIN\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]},{\"center_id\":562266,\"name\":\"CHC BISRAKH\",\"address\":\"PHCBISRAKHGBNAGAR\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":0,\"long\":0,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"793ea833-eb5b-4a93-b268-c6cb7a5c4d38\",\"date\":\"09-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"c4197409-3017-4dbe-881a-a1945692645d\",\"date\":\"10-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"1e7df563-79bf-465c-bdd4-49c7782fd793\",\"date\":\"11-05-2021\",\"available_capacity\":10,\"min_age_limit\":18,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"f570b34c-405b-4862-837f-1604c7daaab4\",\"date\":\"12-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"6a5874ff-fa87-4553-bd20-73af44725c75\",\"date\":\"13-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"52437f33-c21b-4b98-b740-7ead3c941342\",\"date\":\"15-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]},{\"center_id\":608133,\"name\":\"Fortis Hospital PVT\",\"address\":\"Rasoolpur Nawada Industrial Area Sector 62 NOIDA\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"08:30:00\",\"to\":\"14:30:00\",\"fee_type\":\"Paid\",\"sessions\":[{\"session_id\":\"2e09ae6f-a122-4308-ada9-0fad6dc10f66\",\"date\":\"09-05-2021\",\"available_capacity\":0,\"min_age_limit\":18,\"vaccine\":\"COVAXIN\",\"slots\":[\"08:30AM-09:30AM\",\"09:30AM-10:30AM\",\"10:30AM-11:30AM\",\"11:30AM-02:30PM\"]}],\"vaccine_fees\":[{\"vaccine\":\"COVAXIN\",\"fee\":\"1250\"}]},{\"center_id\":602641,\"name\":\"DISTRICT COMBINED HOSPITAL\",\"address\":\"Sec 30 Noida\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"District Hospital\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"06020e2c-85ce-4d4d-bef6-27cab4b143c8\",\"date\":\"09-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"313857f7-9a3a-4ef3-8cac-5a174e26daee\",\"date\":\"10-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"f3bc858f-aac7-4ac5-9244-6e2d2f0b61b1\",\"date\":\"11-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"29f83715-88ac-47e6-8f1a-257d5e50e659\",\"date\":\"12-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"72e64f6d-2d7d-48d7-b3fd-112ea4e7d6cc\",\"date\":\"13-05-2021\",\"available_capacity\":7,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"8da8b127-1349-433d-9156-17eb9ccc5ee6\",\"date\":\"15-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]},{\"center_id\":568853,\"name\":\"Esic Noida\",\"address\":\"Noida Sec 24\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"NOIDA\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"2af81974-2f4c-4173-8fea-e371c212e2a0\",\"date\":\"09-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"fcf91b1c-00af-4593-8e8e-f4464b1e2ece\",\"date\":\"10-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"fae42499-80c3-4b3c-ad74-189883774313\",\"date\":\"11-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]},{\"center_id\":682515,\"name\":\"MAX MULTISPECIALITY CENTRE\",\"address\":\"A 364 SECTOR 19 NOIDA\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"09:00:00\",\"to\":\"18:00:00\",\"fee_type\":\"Paid\",\"sessions\":[{\"session_id\":\"5a6652bd-263f-4fe2-afd9-152ef9e4ff4f\",\"date\":\"10-05-2021\",\"available_capacity\":10,\"min_age_limit\":18,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-06:00PM\"]},{\"session_id\":\"c7ff6bb1-5ad1-438e-8b68-3a414357fab1\",\"date\":\"11-05-2021\",\"available_capacity\":0,\"min_age_limit\":18,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-06:00PM\"]},{\"session_id\":\"b983ddc6-eb07-44b2-b219-6276afecc45e\",\"date\":\"12-05-2021\",\"available_capacity\":0,\"min_age_limit\":18,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-06:00PM\"]},{\"session_id\":\"1cb2da2f-4b0d-40a1-930e-a0369489e779\",\"date\":\"13-05-2021\",\"available_capacity\":0,\"min_age_limit\":18,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-06:00PM\"]},{\"session_id\":\"498df287-6837-4dbe-9ac3-dcd932d4689a\",\"date\":\"14-05-2021\",\"available_capacity\":0,\"min_age_limit\":18,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-06:00PM\"]},{\"session_id\":\"3414cacc-43af-4aa3-8d71-2547ae87a3e2\",\"date\":\"15-05-2021\",\"available_capacity\":0,\"min_age_limit\":18,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-06:00PM\"]}],\"vaccine_fees\":[{\"vaccine\":\"COVISHIELD\",\"fee\":\"900\"}]},{\"center_id\":609930,\"name\":\"PHC MAMURA\",\"address\":\"PHC MAMURA\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"84a80027-5fef-49f0-8f8d-fdfee73627a9\",\"date\":\"10-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"23d4c870-420e-482f-bd2d-0e9c53487768\",\"date\":\"11-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"c44666ed-f230-46d5-b43f-6f983626895b\",\"date\":\"12-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"7d5e3137-b40b-4eb0-9be1-c0a7cba74c04\",\"date\":\"13-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"c6821736-ef57-4cd1-b9fe-ed7e12f32f73\",\"date\":\"15-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]},{\"center_id\":563029,\"name\":\"Chc Bhangel\",\"address\":\"SEC 110 NOIDA G.B.NAGAR\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"407aa4ac-fe55-488b-8bce-1b0e2d7308c1\",\"date\":\"10-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"4d46f177-304a-4a66-8281-9e025154531a\",\"date\":\"11-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"2067674d-097c-4dff-95a6-ac7bc9ccd9e2\",\"date\":\"12-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"c4747dd3-2e72-4ce6-80ba-31cce8c04e50\",\"date\":\"13-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"5a707e4b-0631-4dad-b203-5341bbbca100\",\"date\":\"15-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]},{\"center_id\":562511,\"name\":\"S S P G I\",\"address\":\"SECTOR-30, NOIDA\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"d90ea2bf-5a70-4585-b1fd-565b9cf9dba8\",\"date\":\"10-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"b069b2f8-ebdd-4e00-9445-1c1a60af5b9a\",\"date\":\"11-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"06bbfb03-6c83-44c7-9e0b-fac5829e9a69\",\"date\":\"12-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"897bf057-743d-4a7e-8c81-44e80d9b9857\",\"date\":\"13-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"57057871-2246-42e3-837e-bdcf64556e3b\",\"date\":\"15-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]},{\"center_id\":609976,\"name\":\"PHC BAROLA\",\"address\":\"PHC BAROLA\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"90f57c1d-f38c-4da6-b53a-04db8644bbe1\",\"date\":\"10-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"6b591e33-0645-4c37-8c85-69cf6e50cc53\",\"date\":\"11-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"890649cd-f574-4b2f-a814-a6402ce3535f\",\"date\":\"12-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"0fd05e05-3610-4fcd-80fe-589dea739c76\",\"date\":\"13-05-2021\",\"available_capacity\":7,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"a22a7fd4-080a-45bc-a20c-91a6cda8cf1f\",\"date\":\"15-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]},{\"center_id\":610045,\"name\":\"CHC BADALPUR\",\"address\":\"CHC BADALPUR\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":28,\"long\":77,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"53f70bce-6a06-44f4-9eab-d4727f0deb24\",\"date\":\"10-05-2021\",\"available_capacity\":4,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"9f2e9977-8eca-46e3-b9d0-f86e3cfd28c6\",\"date\":\"11-05-2021\",\"available_capacity\":120,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"ff4c93fe-0b13-47fe-9c9f-1aca1843d3a6\",\"date\":\"12-05-2021\",\"available_capacity\":112,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"2c2b1c51-fe44-4822-9e30-b451a192159d\",\"date\":\"13-05-2021\",\"available_capacity\":110,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"cc0874cf-3d6f-4584-b807-34cd001ec58d\",\"date\":\"15-05-2021\",\"available_capacity\":63,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]},{\"center_id\":569387,\"name\":\"PHC BISRAKH\",\"address\":\"CHC BISRAKH\",\"state_name\":\"Uttar Pradesh\",\"district_name\":\"Gautam Buddha Nagar\",\"block_name\":\"Bisrakh\",\"pincode\":201301,\"lat\":0,\"long\":0,\"from\":\"09:00:00\",\"to\":\"17:00:00\",\"fee_type\":\"Free\",\"sessions\":[{\"session_id\":\"be0c0f5a-b013-41de-a58f-c2547eb06f09\",\"date\":\"10-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"2b9decfd-8765-4dbe-8ef3-f90b53f6de6b\",\"date\":\"11-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"1336a91b-d6a0-4769-b696-318fae63c2c3\",\"date\":\"12-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"025a057d-38e0-4192-a551-05fe5a36e7f1\",\"date\":\"13-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]},{\"session_id\":\"80efbec1-a2c5-4e90-915a-856c56ff113b\",\"date\":\"15-05-2021\",\"available_capacity\":0,\"min_age_limit\":45,\"vaccine\":\"COVISHIELD\",\"slots\":[\"09:00AM-11:00AM\",\"11:00AM-01:00PM\",\"01:00PM-03:00PM\",\"03:00PM-05:00PM\"]}]}]}";
//		CowinResponse testResponse = mapper.readValue(json, CowinResponse.class);
		log.info("Inside findSlotsConsumer...");
		String pincode = pincodeQueue.take();
//		String pincode = "201301";
		log.info("Start finding slots for pincode : {}",pincode);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate today = LocalDate.now();
		boolean found = false;
		int count = 0;
		int capacity = 0;
		for(int i = 0; i < 4; i ++) {
			log.info("Checking coWIN website for slots for pin : {} and date : {}", pincode, today);
			CowinResponse resp = findSlots(pincode, today.format(formatter));
//			CowinResponse resp = testResponse;
			if(resp == null) {
				log.info("Null response from coWIN fetch");
				return;
			}
			for(Center center : resp.getCenters()) {
				for(Session session : center.getSessions()) {
					if(session.getMinAgeLimit() != null && session.getAvailableCapacity() != null 
							&& 18 == session.getMinAgeLimit() && session.getAvailableCapacity() > 0) {
						log.info("Slots found for age 18+");
						capacity += session.getAvailableCapacity();
						count ++;
						found = true;
					} else {
//						log.info("No slots found for age 18+ now...");
					}
				}
			}
			today = today.plusDays(6);
		}
		if(found) {
			log.info("Available capacity = {}", capacity);
			log.info("Total centers = {}", count);
			String text = "At pincode " + pincode + " there are " + capacity + " slots available.";
//			musicService.textToSpeech(text);
			musicService.playMusic();
		}

	}
	
	private CowinResponse findSlots(String pin, String date) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(finderUrl).queryParam("pincode", pin).queryParam("date", date).build();
		HttpEntity<Object> en = new HttpEntity<Object>(headers);
		ResponseEntity<CowinResponse> response = null;
		try {
			response = rest.exchange(uri.toUriString(), HttpMethod.GET, en, CowinResponse.class);
		} catch(Exception e) {
			log.info("coWIN exception message: {}", e.getMessage());
			return null;
		}
		log.info("coWIN reponse: {}", response.getStatusCodeValue());
		return response.getBody();
	}

}
