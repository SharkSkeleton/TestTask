package unitask;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class Resource {

    private static final String API_CONTEXT = "/api/v1";

    private final Service shapeService;

    public Resource(Service shapeService) {
        this.shapeService = shapeService;
        setupEndpoints();
    }

    private void setupEndpoints() {
        post(API_CONTEXT + "/shapes", "application/json", (request, response) -> {
            shapeService.createNewShape(request.body());
            response.status(201);
            return response;
        }, new JsonTransformer());

        get(API_CONTEXT + "/shapes/:id", "application/json", (request, response)

                -> shapeService.find(request.params(":id")), new JsonTransformer());

        get(API_CONTEXT + "/shapes", "application/json", (request, response)

                -> shapeService.findAll(), new JsonTransformer());

        put(API_CONTEXT + "/shapes/:id", "application/json", (request, response)

                -> shapeService.update(request.params(":id"), request.body()), new JsonTransformer());
    }


}
