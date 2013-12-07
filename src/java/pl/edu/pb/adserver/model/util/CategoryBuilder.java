package pl.edu.pb.adserver.model.util;

import java.util.LinkedList;
import java.util.List;
import pl.edu.pb.adserver.model.Category;

class OptionStrategy implements BuildStrategy
{
    @Override
    public void before(Category current, CategoryBuilder context)
    {
        context.output += "<li>" + current.getName() + "<ul>";
    }
    
    @Override
    public void after(Category current, CategoryBuilder context)
    {
        context.output += "</ul></li>";
    }
}

class ListStrategy implements BuildStrategy
{
    List<Category> category;
    
    public ListStrategy()
    {
        category = new LinkedList<Category>();
    }

    @Override
    public void before(Category current, CategoryBuilder context) {
        category.add(current);
    }

    @Override
    public void after(Category current, CategoryBuilder context) {
        context.objectory_output = category;
    }
    
    
}

interface BuildStrategy
{
    public void before(Category current, CategoryBuilder context);
    public void after(Category current, CategoryBuilder context);
}

public class CategoryBuilder {
    Category root;
    BuildStrategy bs;
    String output;
    Object objectory_output = null;
    
    public CategoryBuilder(Category root)
    {
        this.root = root;
        this.bs = new OptionStrategy();
    }
    
    public CategoryBuilder(Category root, String otherBuild)
    {
        this.root = root;
        this.bs = new ListStrategy();
    }
    
    public void doWork(Category current)
    {
        bs.before(current, this);
        for(Category c : current.getChilds())
            doWork(c);
        bs.after(current, this);
    }
    
    public String getResult()
    {
        if(output == null)
        {
            output = "";
            doWork(root);
        }
        return output;
    }
    
    public Object getResultObject()
    {
        if(objectory_output == null)
        {
            doWork(root);
        }
        return objectory_output;
    }
}
