package kalpas.insta.stats;

import java.util.Date;

import kalpas.insta.api.Users;
import kalpas.insta.api.domain.UserData;
import kalpas.insta.stats.IO.GmlWriter;
import kalpas.insta.stats.graph.GmlGraph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Sets;

@Controller
@RequestMapping("/graph")
public class GraphBuilderController {

    private final Log    logger       = LogFactory.getLog(getClass());

    private GraphBuilder graphBuilder = new GraphBuilder();
    private GmlWriter    writer       = new GmlWriter();

    private Users        users        = new Users();

    @RequestMapping(method = RequestMethod.GET)
    public String build(@RequestParam(value = "access_token", required = true) String access_token,
            @RequestParam(value = "id", required = true) String id, ModelMap model) {
        String fileName = "graph" + new Date().getTime();

        UserData user = users.get(id, access_token);
        if (user != null) {
            GmlGraph graph = GmlGraph.build(graphBuilder.buildGraph(user, access_token));
            writer.saveGraphToFile(fileName, graph, Sets.newHashSet("id", "username"));

            model.addAttribute("file", fileName);
            return "graph";
        } else {
            String errorMessage = String.format("User ID \"%s\" is not valid", id);
            logger.info(errorMessage);
            model.addAttribute("error", errorMessage);
            return "error";
        }
    }

}
