package pl.edu.agh.student.dejakraj.tvprogramme;

public class Program {
    private String name;
    private String hour;

    public Program(String _name, String _hour)
    {
        name = _name;
        hour = _hour;
    }

    public String getName()
    {
        return name;
    }

    public  String getHour()
    {
        return hour;
    }

}
