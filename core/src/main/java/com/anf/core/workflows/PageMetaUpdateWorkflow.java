package com.anf.core.workflows;


import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.anf.core.services.ResourceResolverService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.Objects;

import static com.day.cq.wcm.api.NameConstants.NT_PAGE;

/**
 * Page Metadata workflow
 */

/*** Begin Code - Manikandan Annamalai ***/

@Component(service = WorkflowProcess.class, property = {"process.label=ANF: Page MetaData update workflow"})
    public class PageMetaUpdateWorkflow implements WorkflowProcess {

    protected Logger LOG = LoggerFactory.getLogger(PageMetaUpdateWorkflow.class);

    @Reference
    private ResourceResolverService resourceResolverService;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {

        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            String payload = (String) workflowData.getPayload().toString();

            ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);
            Resource resource = resourceResolver.getResource(payload);

            if(Objects.requireNonNull(resource).getResourceType().equals(NT_PAGE)) {
                Session session = workflowSession.adaptTo(Session.class);
                Node paloadNode = session.getNode(resource.getPath());

                Node someNode = paloadNode.getNode(Node.JCR_CONTENT);
                someNode.setProperty("pageCreated", true);

                LOG.debug("pageCreated property added to {}", payload);
            }

            LOG.info("Workflow Data {}", payload);
        }catch (Exception e ){
            LOG.error("Exception: {}", e.getMessage());
        }
    }
}
