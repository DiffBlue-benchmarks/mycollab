/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.cases;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.form.view.DynaFormLayout;
import com.esofthead.mycollab.mobile.module.crm.events.CaseEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData;
import com.esofthead.mycollab.mobile.module.crm.view.activity.ActivityRelatedItemView;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewComponent
public class CaseReadViewImpl extends AbstractPreviewItemComp<SimpleCase> implements CaseReadView {
    private static final long serialVersionUID = -983883973494397334L;
    protected ActivityRelatedItemView associateActivities;
    protected CaseRelatedContactView associateContacts;

    @Override
    public HasPreviewFormHandlers<SimpleCase> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void initRelatedComponents() {
        associateActivities = new ActivityRelatedItemView(CrmTypeConstants.CASE);
        associateContacts = new CaseRelatedContactView();
    }

    @Override
    protected void afterPreviewItem() {
        associateActivities.displayActivity(beanItem.getId());
        associateContacts.displayContacts(beanItem);
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getSubject();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleCase> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.CASE, CaseDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleCase> initBeanFormFieldFactory() {
        return new CaseReadFormFieldFactory(previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm)
                .createButtonControls(RolePermissionCollections.CRM_CASE);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        HorizontalLayout toolbarLayout = new HorizontalLayout();
        toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        toolbarLayout.setSpacing(true);

        Button relatedContacts = new Button();
        relatedContacts.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_CONTACT
                + "\"></span><div class=\"screen-reader-text\">"
                + AppContext.getMessage(ContactI18nEnum.LIST)
                + "</div>");
        relatedContacts.setHtmlContentAllowed(true);
        relatedContacts.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 7589415773039335559L;

            @Override
            public void buttonClick(ClickEvent arg0) {
                EventBusFactory.getInstance().post(new CaseEvent.GoToRelatedItems(this,
                        new CrmRelatedItemsScreenData(associateContacts)));
            }
        });
        toolbarLayout.addComponent(relatedContacts);

        Button relatedActivities = new Button();
        relatedActivities.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_ACTIVITY
                + "\"></span><div class=\"screen-reader-text\">"
                + AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY)
                + "</div>");
        relatedActivities.setHtmlContentAllowed(true);
        relatedActivities.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 7589415773039335559L;

            @Override
            public void buttonClick(ClickEvent arg0) {
                EventBusFactory.getInstance().post(
                        new CaseEvent.GoToRelatedItems(this, new CrmRelatedItemsScreenData(associateActivities)));
            }
        });
        toolbarLayout.addComponent(relatedActivities);

        return toolbarLayout;
    }

    @Override
    public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
        return associateActivities;
    }

    @Override
    public IRelatedListHandlers<SimpleContact> getRelatedContactHandlers() {
        return associateContacts;
    }

}
