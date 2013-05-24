package be.vrt.web.restdc.spring;

import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.store.DocumentSetStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

/**
 * A Spring MVC RESTful Controller, exposing the document set in a {@link be.vrt.web.restdc.store.DocumentSetStore}.
 *
 * @author Mike Seghers
 */
@Controller
public class RestDcController {
    private DocumentSetStore documentSetStore;

    private String htmlViewName = "documentSet";
    private String htmlModelName = "documentSet";

    /**
     * Constructor, initializing the controller with the DocumentSetStore to use to lookup {@link be.vrt.web.restdc.domain.DocumentSet}s
     * @param documentSetStore the store in which {@link be.vrt.web.restdc.domain.DocumentSet}s can be found.
     */
    public RestDcController(final DocumentSetStore documentSetStore) {
        this.documentSetStore = documentSetStore;
    }

    /**
     * Get all document sets and put them in the body of the response.
     * @return all available document sets
     */
    @RequestMapping(value = "/api/dc", produces = {"application/json", "application/xml"})
    @ResponseBody
    public Collection<DocumentSet> getAllDocumentSets() {
        return documentSetStore.findAllDocumentSets();
    }

    /**
     * Get all document sets and return them as model and view.
     * @return the model and view
     */
    @RequestMapping(value = "/api/dc", produces = {"text/html"})
    public ModelAndView getAllDocumentSetsAsModelAndView() {
        Collection<DocumentSet> allDocumentSets = documentSetStore.findAllDocumentSets();
        return new ModelAndView(htmlViewName, htmlModelName, allDocumentSets);
    }

    public void setHtmlViewName(final String htmlViewName) {
        this.htmlViewName = htmlViewName;
    }

    public void setHtmlModelName(final String htmlModelName) {
        this.htmlModelName = htmlModelName;
    }
}
