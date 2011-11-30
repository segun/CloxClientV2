/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloxclient.models;

import java.util.Date;

/**
 *
 * @author segun
 */
public class Message {

    private String from;
    private String to;
    private String msg;
    private Date time;    

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
    
    
    @Override
    public String toString() {
        return "<a name='curpos'><i>" + this.getTime() + "</i> <b style='color:green'>from " + this.getFrom() + "</b> " +
                "<b style='color:red'>to " + this.getTo() + "</b><br />" +
                "<div style='margin-left:25px'>" + this.getMsg() + "</div><br /></a>";
    }
}
