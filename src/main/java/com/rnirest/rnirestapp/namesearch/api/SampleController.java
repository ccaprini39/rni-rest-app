// package gov.doj.fbi.ncic.knsquery.api;

// import gov.doj.fbi.ncic.knsmodels.SearchResult;
// import gov.doj.fbi.ncic.knsquery.service.KnsQueryService;
// import gov.doj.fbi.ncic.knsmodels.NameSearchRequestObject;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import io.swagger.annotations.ApiResponse;
// import io.swagger.annotations.ApiResponses;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Profile;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;
// import javax.validation.constraints.NotNull;
// import java.util.*;

// @RestController
// @CrossOrigin(origins = "${client.url}")
// @Api(value="Key Name Search - Query Microservice",
//         description="Operations pertaining to the querying of associated search key(s) from a given name + DOB request.",
//         tags = {"knsquery"})
// @RequestMapping("${microservice.endpoint}")
// public class KnsQueryController {

//     private final KnsQueryService knsQueryService;

//     @Autowired
//     public KnsQueryController(KnsQueryService knsQueryService) {
//         this.knsQueryService = knsQueryService;
//     }

//     @ApiOperation(value = "Posts an incoming internal key name search query request to " +
//             "query against the associated search key(s).",
//             tags = {"knsquery"})
//     @ApiResponses(value = {
//             @ApiResponse(code = 201, message = "Successfully received internal key name search query request.")
//     })
//     @PostMapping(value = "/internal", headers = "Content-Type=application/json")
//     @Profile({"dev", "staging"})
//     public ResponseEntity<List<SearchResult>> receiveInternalQueryRequest(@Valid @NotNull @RequestBody NameSearchRequestObject msg) {
//         return knsQueryService.receiveInternalQueryRequest(msg);
//     }

//     @PostMapping(value = "/internal/batch", headers = "Content-Type=application/json")
//     @Profile({"dev", "staging"})
//     public ResponseEntity<List<SearchResult>> receiveInternalBatchQueryRequest(@Valid @NotNull @RequestBody List<NameSearchRequestObject> queries) {
//         return knsQueryService.receiveInternalBatchQueryRequest(queries);
//     }

//     @GetMapping(value = "/internal/list", headers = "Content-Type=application/json")
//     @Profile({"dev", "staging"})
//     public ResponseEntity<List<SearchResult>> receiveInternalListRequest() {
//         return knsQueryService.receiveInternalListRequest();
//     }

// }