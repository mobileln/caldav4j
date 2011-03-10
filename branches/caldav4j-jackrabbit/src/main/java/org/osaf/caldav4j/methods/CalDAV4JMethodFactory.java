/*
 * Copyright 2005 Open Source Applications Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osaf.caldav4j.methods;

import java.io.IOException;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import org.osaf.caldav4j.CalDAVConstants;
import org.osaf.caldav4j.model.request.TicketRequest;

public class CalDAV4JMethodFactory {

    protected String prodID = CalDAVConstants.PROC_ID_DEFAULT;
    private boolean validatingOutputter = false;
    
    private ThreadLocal<CalendarBuilder> calendarBuilderThreadLocal = new ThreadLocal<CalendarBuilder>();
    private CalendarOutputter calendarOutputter = null;
    
    public CalDAV4JMethodFactory(){
        
    }
    
    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public PutMethod createPutMethod(){
        PutMethod putMethod = new PutMethod();
        putMethod.setProcID(prodID);
        putMethod.setCalendarOutputter(getCalendarOutputterInstance());
        return putMethod;
    }
    
    public PostMethod createPostMethod(){
        PostMethod postMethod = new PostMethod();
        postMethod.setProcID(prodID);
        postMethod.setCalendarOutputter(getCalendarOutputterInstance());
        return postMethod;
    }
    
    public MkCalendarMethod createMkCalendarMethod(String uri){
        MkCalendarMethod mkCalendarMethod = new MkCalendarMethod(uri);
        return mkCalendarMethod;
    }
    
    public MkTicketMethod createMkTicketMethod(String uri, TicketRequest ticketRequest){
        MkTicketMethod mkTicketMethod = new MkTicketMethod(uri,ticketRequest);
        return mkTicketMethod;
    }
    
    public DelTicketMethod createDelTicketMethod(String uri, String ticket){
        DelTicketMethod delTicketMethod = new DelTicketMethod(uri,ticket);
        return delTicketMethod;
    }
    
    public PropFindMethod createPropFindMethod(String uri) throws IOException{
        PropFindMethod propFindMethod = new PropFindMethod(uri);
        return propFindMethod;
        
    }
    
    public GetMethod createGetMethod(){
        GetMethod getMethod = new GetMethod();
        getMethod.setCalendarBuilder(getCalendarBuilderInstance());
        return getMethod;
    }
    
    public CalDAVReportMethod createCalDAVReportMethod(String uri){
        CalDAVReportMethod reportMethod = new CalDAVReportMethod(uri);
        reportMethod.setCalendarBuilder(getCalendarBuilderInstance());
        return reportMethod;
    }
    
    public boolean isCalendarValidatingOutputter() {
        return validatingOutputter;
    }

    public void setCalendarValidatingOutputter(boolean validatingOutputter) {
        this.validatingOutputter = validatingOutputter;
    }
    
    
    protected synchronized CalendarOutputter getCalendarOutputterInstance(){
        if (calendarOutputter == null){
            calendarOutputter = new CalendarOutputter(validatingOutputter);
        }
        return calendarOutputter;
    }
    
    private CalendarBuilder getCalendarBuilderInstance(){
        CalendarBuilder builder = calendarBuilderThreadLocal.get();
        if (builder == null){
            builder = new CalendarBuilder();
            calendarBuilderThreadLocal.set(builder);
        }
        return builder;
    }
}