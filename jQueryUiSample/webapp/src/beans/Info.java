package beans;

public class Info
{
	
	private String id_info;
	private String names;
	
	public Info()
	{
	}
	
	public Info(String id, String names)
	{
		this.setId_info(id);
		this.setNames(names);
	}
	
	public String getNames()
	{
		return names;
	}
	
	public void setNames(String names)
	{
		this.names = names;
	}
	
	public String getId_info()
	{
		return id_info;
	}
	
	public void setId_info(String id_info)
	{
		this.id_info = id_info;
	}
}
