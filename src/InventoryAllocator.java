
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryAllocator {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

		InventoryAllocator t = new InventoryAllocator();
		t.test1();
		t.test2();
		t.test3();
		t.test4();
		t.test5();
	}

	/**
	 * 
	 * @param inputCustomerRequest
	 * @param storeInventory
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private void mainFunction(String inputCustomerRequest, String storeInventory)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Integer> input = mapper.readValue(inputCustomerRequest.getBytes(), HashMap.class);
		ArrayList<HashMap<String, Object>> inventory = mapper.readValue(storeInventory.getBytes(), ArrayList.class);

		ArrayList<HashMap<String, Object>> response = new ArrayList<>();

		for (String product : input.keySet()) {
			int requestedCount = input.get(product);

			for (HashMap<String, Object> store : inventory) {
				String storeName = (String) store.get("name");
				HashMap<String, Integer> storeInfo = (HashMap) store.get("inventory");

				if (storeInfo.containsKey(product)) {
					if (requestedCount <= storeInfo.get(product)) {
						
						HashMap<String, Object> output = new HashMap();
						HashMap<Object, Object> requiredItem = new HashMap();
						requiredItem.put(product, requestedCount);
						output.put(storeName, requiredItem);
						response.add(output);

						break;
					} else if (requestedCount > storeInfo.get(product)) {
						requestedCount = requestedCount - storeInfo.get(product);
						HashMap<String, Object> output = new HashMap();
						HashMap<Object, Object> requiredItem = new HashMap();
						requiredItem.put(product, requestedCount);
						output.put(storeName, requiredItem);
						response.add(output);
						
					}

				}
			}
		}
		System.out.println(response);

	}

	private void test1() throws JsonParseException, JsonMappingException, IOException {
		System.out.println("this is a positive case where input is  satisfing all the requirments And customer have multiple items ");
		String inputCustomerRequest = "{\"apple\": 1, \"orange\":2,\"banana\":3}";
		String storeInventory = "[{ \"name\": \"owd\", \"inventory\": { \"apple\": 10 ,\"orange\":2} }, { \"name\": \"dm\", \"inventory\": { \"apple\": 30, \"orange\": 10,\"banana\": 10 } }]";

		this.mainFunction(inputCustomerRequest, storeInventory);
	}

	private void test2() throws JsonParseException, JsonMappingException, IOException {
		System.out.println("this is a semi-positive case, customer did not get the full order just the partial order");
		String inputCustomerRequest = "{\"apple\": 30, \"orange\":7}";
		String storeInventory = "[{ \"name\": \"owd\", \"inventory\": { \"apple\": 10 } }, { \"name\": \"dm\", \"inventory\": { \"orange\": 10 } }]";

		this.mainFunction(inputCustomerRequest, storeInventory);

	}

	private void test3() throws JsonParseException, JsonMappingException, IOException {
		System.out.println("this is a positive case where we satisfing the requirment ");
		String inputCustomerRequest = "{ \"orange\":2}";
		String storeInventory = "[{ \"name\": \"owd\", \"inventory\": { \"apple\": 10 } }, { \"name\": \"dm\", \"inventory\": { \"orange\": 10 } }]";

		this.mainFunction(inputCustomerRequest, storeInventory);

	}

	private void test4() throws JsonParseException, JsonMappingException, IOException {
		System.out.println("there is no customer input and the API is invoked");
		String inputCustomerRequest = "{ }";
		String storeInventory = "[{ \"name\": \"owd\", \"inventory\": { \"apple\": 10 } }, { \"name\": \"dm\", \"inventory\": { \"orange\": 10 } }]";

		this.mainFunction(inputCustomerRequest, storeInventory);

	}
	private void test5() throws JsonParseException, JsonMappingException, IOException {
		System.out.println("the customer requested an item which was not in the storage ");
		String inputCustomerRequest = "{ \"Pineapple\":10}";
		String storeInventory = "[{ \"name\": \"owd\", \"inventory\": { \"apple\": 10 } }, { \"name\": \"dm\", \"inventory\": { \"orange\": 10 } }]";

		this.mainFunction(inputCustomerRequest, storeInventory);

	}
}
