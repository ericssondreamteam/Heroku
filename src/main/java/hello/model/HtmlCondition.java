package hello.model;

public class HtmlCondition
{
    private static String condition="false";

    public  static String getCondition()
    {
        return condition;
    }

    public static void setCondition(String condition)
    {
        HtmlCondition.condition = condition;
    }

    public  HtmlCondition(String condition)
    {
        HtmlCondition.condition = condition;
    }

    public HtmlCondition()
    {
    }
}
