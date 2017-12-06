package life.qbic;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import presenter.MainPresenter;
import view.MainView;

@Theme("mytheme")
@SuppressWarnings("serial")
@Widgetset("life.qbic.AppWidgetSet")
public class MyPortletUI extends UI {

    private static final Log log = LogFactoryUtil.getLog(MyPortletUI.class.getName());

    @Override
    protected void init(VaadinRequest request) {

        MainView mainView = new MainView(this);
        MainPresenter mainPresenter = new MainPresenter(mainView, this);
    }
}
