package pl.edu.pb.adserver.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Ad category class - with inheritances.
 * @author dawid
 */
@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    
    @Column(unique = true)
    protected String name;
    
    @ManyToOne
    protected Category parent;
    
    @OneToMany
    protected List<Category> childs;
    
    public Category()
    {
        this.childs = new LinkedList<Category>();
    }
    
    public Category(String name, Category parent)
    {
        this.name = name;
        this.parent = parent;
        this.childs = new LinkedList<Category>();
    }
    
    public Category(String name)
    {
        this.name = name;
        this.parent = null;
        this.childs = new LinkedList<Category>();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChilds() {
        return childs;
    }

    public void setChilds(List<Category> childs) {
        this.childs = childs;
    }
    
    
}
