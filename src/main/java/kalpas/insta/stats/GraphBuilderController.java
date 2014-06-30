package kalpas.insta.stats;

import java.util.Date;

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
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String build(@RequestParam(value = "access_token", required = true) String access_token, ModelMap model) {
        String fileName = "graph" + new Date().getTime();


        GmlGraph graph = GmlGraph.build(graphBuilder.buildGraph(null, access_token));
        writer.saveGraphToFile(fileName, graph, Sets.newHashSet("id", "username", "full_name"));

        model.addAttribute("file", fileName);
        return "graph";
    }

}
