package top.berthua;

public class Music {
    private long id;
    private String name;
    private String artists;
    private String ImgUrl;

    public Music(long id,String name, String artists,String ImgUrl){
        this.id = id;
        this.name = name;
        this.artists=artists;
        this.ImgUrl = ImgUrl;
    }

    @Override
    public String toString(){
        return name+"-----"+artists+",id="+id+"\n";
    }

    public long getId() { return id ;}

    public String getName() {
        return name;
    }

    public String getArtists() {
        return artists;
    }

    public String getImgUrl() {
        return ImgUrl;
    }
}
