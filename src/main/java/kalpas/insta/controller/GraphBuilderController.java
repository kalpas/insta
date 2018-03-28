package kalpas.insta.controller;

import com.google.common.collect.Sets;
import kalpas.insta.AppConsts;
import kalpas.insta.api.UsersApi;
import kalpas.insta.api.domain.UserData;
import kalpas.insta.stats.GraphBuilder;
import kalpas.insta.stats.IO.GmlWriter;
import kalpas.insta.stats.graph.GmlGraph;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/graph")
public class GraphBuilderController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private GraphBuilder graphBuilder;

    @Autowired
    private GmlWriter writer;

    @Autowired
    private UsersApi usersApi;

    @RequestMapping(method = RequestMethod.GET)
    public String build(@RequestParam(value = "access_token", required = true) String access_token,
                        @RequestParam(value = "id", required = true) Long id,
                        @RequestParam(value = "level", required = false) Integer level, ModelMap model) {

        String fileName = AppConsts.ROOT_PATH + "graph" + new Date().getTime();

        UserData user = usersApi.get(id, access_token);
        if (user != null) {
            if (level == null || level == 1) {
                // TODO testing 2nd version of getGraph
                GmlGraph graph = GmlGraph.build(graphBuilder.buildGraph(user, access_token));
                writer.saveGraphToFile(fileName, graph, Sets.newHashSet("id", "username"));
            } else if (level == 2) {
                GmlGraph graph = GmlGraph.build(graphBuilder.buildGraphLevel2(user, access_token));
                writer.saveGraphToFile(fileName, graph, Sets.newHashSet("id", "username"));
            }

            model.addAttribute("file", fileName);
            return "graph";
        } else {
            String errorMessage = String.format("User ID \"%s\" is not valid. check for error above", id);
            logger.error(errorMessage);
            model.addAttribute("error", errorMessage);
            return "error";
        }
    }

}
