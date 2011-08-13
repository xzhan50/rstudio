package org.rstudio.studio.client.workbench.codesearch.ui;

import org.rstudio.core.client.widget.CanFocus;
import org.rstudio.core.client.widget.ModalDialogBase;
import org.rstudio.studio.client.workbench.codesearch.CodeSearch;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Provider;

public class CodeSearchDialog extends ModalDialogBase 
                              implements CodeSearch.Observer

{
   public CodeSearchDialog(Provider<CodeSearch> pCodeSearch)
   {
      super();
      
      setGlassEnabled(false);
      setAutoHideEnabled(true);
      
      setText("Go to File/Function");
      
      pCodeSearch_ = pCodeSearch;
   }
   
   @Override
   protected Widget createMainWidget()
   {     
      VerticalPanel mainPanel = new VerticalPanel();
      mainPanel.addStyleName(
         CodeSearchResources.INSTANCE.styles().codeSearchDialogMainWidget());
      codeSearch_ = pCodeSearch_.get();
      codeSearch_.setObserver(this);
      mainPanel.add(codeSearch_.getSearchWidget());
      return mainPanel;
   }
   
   @Override
   protected void positionAndShowDialog()
   {
      setPopupPositionAndShow(new PositionCallback() {
         @Override
         public void setPosition(int offsetWidth, int offsetHeight)
         {
            int left = (Window.getClientWidth()/2) - (offsetWidth/2);
            setPopupPosition(left, 15);
            
         }
         
      });
   }
   
   @Override
   protected void onDialogShown()
   { 
      ((CanFocus)codeSearch_.getSearchWidget()).focus();
   }
   
   @Override
   protected void onEscapeKeyDown(Event.NativePreviewEvent event)
   {
      // close dialog on ESC -- delay so that the ESC key doesn't 
      // end up in the editor
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
         @Override
         public void execute()
         {
            closeDialog();
         }
      });
     
   }
   
   @Override
   public void onCompleted()
   {
      closeDialog();  
   }
   
   @Override
   public String getCueText()
   {
      return "";
   }
  
   Provider<CodeSearch> pCodeSearch_;
   CodeSearch codeSearch_;
   
   
}