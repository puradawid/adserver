package pl.edu.pb.adserver.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

/**
 * This class represents a row in ads model - single ad
 */
@Entity
public class Ad implements Serializable {
    /** id for ad */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    
    /** Html content for ad */
    protected String content;
    
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "user")
    /** Client id which add ad */
    protected User user;
    
    /** Content */
    public enum ContentType
    {
        html,
        picture;
        public static ContentType parse(String content)
        {
            if(content.equals("html")) return html;
            if(content.equals("picture")) return picture;
            return null;
        }
    };
    
    /** Category od ad type (without a tree) */
    protected String category;
    
    /** Is this an picture or plain html? */
    protected ContentType contentType;
    
    public Ad() {} //default but certainly unused constructor
    
    public Ad(int id, String content, ContentType contentType, User user,
            String category)
    {
        this.id = id;
        this.content = content;
        this.contentType = contentType;
        this.category = category;
        this.user = user;
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void setContentType(ContentType content)
    {
        this.contentType = content;
    }
    
    public ContentType getContentType()
    {
        return this.contentType;
    }
    
    public String getCategory()
    {
        return category;
    }
    
    public void setCategory(String category)
    {
        this.category = category;
    }
}
