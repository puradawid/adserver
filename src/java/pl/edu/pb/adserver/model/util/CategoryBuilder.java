package pl.edu.pb.adserver.model.util;

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

interface BuildStrategy
{
    public void before(Category current, CategoryBuilder context);
    public void after(Category current, CategoryBuilder context);
    
    
}

public class CategoryBuilder {
    Category root;
    BuildStrategy bs;
    String output;
    
    public CategoryBuilder(Category root)
    {
        this.root = root;
        this.bs = new OptionStrategy();
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
}
