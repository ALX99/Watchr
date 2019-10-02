package ipren.watchr.dataHolders;

public class Movie {
    public double popularity;
    public int voteCount;
    public boolean video;
    public String posterPath;
    public int id;
    public boolean adult;
    public String backdropPath;
    public String originalLanguage;
    public String originalTitle;
    public int[] genreIds;
    public String title;
    public double voteAverage;
    public String overview;
    public String releaseDate;

    public Movie(double popularity, int voteCount, boolean video, String posterPath, int id, boolean adult, String backdropPath, String originalLanguage, String originalTitle, int[] genreIds, String title, double voteAverage, String overview, String releaseDate) {
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.posterPath = posterPath;
        this.id = id;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.title = title;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }
}
