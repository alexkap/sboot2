package ak.challenge.cities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import static ak.challenge.cities.CitiesConstants.ROOT_TEXT;

@RestController
public class CitiesController {
    private static Logger LOG = LoggerFactory.getLogger(CitiesController.class);
    @Autowired
    @Qualifier("navigator")
    CustomMap navigator;

    @RequestMapping()
    public String index() {
        return ROOT_TEXT;
    }

    @GetMapping("/connected")
    public String areConnected(@RequestParam(value = "origin", defaultValue = "adlkakjlfjs") String origin,
                               @RequestParam(value = "destination", defaultValue = "iqrwfjmfemf")  String destination) {
        LOG.debug("origin=" + origin+ "; destination=" + destination);
        if(navigator.areConnected(origin,destination)) return "yes";
        else return "no";
    }

}
