package com.vseminar.config;

import org.jsoup.nodes.Element;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

@SuppressWarnings("serial")
public class VSeminarSessionInitListener implements SessionInitListener {

    @Override
    public final void sessionInit(final SessionInitEvent event) throws ServiceException {
        event.getSession().addBootstrapListener(new BootstrapListener() {
            @Override
            public void modifyBootstrapPage(final BootstrapPageResponse response) {
                final Element head = response.getDocument().head();
                // 모바일 브라우져의 크기에 맞도록 스케일 처리
                head.appendElement("meta")
                        .attr("name", "viewport")
                        .attr("content", "width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no");                
            }
            @Override
            public void modifyBootstrapFragment(final BootstrapFragmentResponse response) {
            }
        });
    }

}
