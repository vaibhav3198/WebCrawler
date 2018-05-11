import org.jsoup.Jsoup;

import javax.lang.model.util.Elements;
import javax.swing.text.Document;
import javax.swing.text.Element;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WebCrawler {

    private final Set<URL> links;  //inventory of all the urls already visited
    private final long startTime;

    private WebCrawler(final URL startURL)
    {
        this.links = new HashSet<>();
        this.startTime = System.currentTimeMillis();
        Crawl(initURLs(startURL));
    }

    private void Crawl(Set<URL> urls) {
        urls.removeAll(this.links);   //remove all visited URLs
        if(!urls.isEmpty()){
            final Set<URL> newURLs = new HashSet<>();
            try {
                this.links.addAll(urls);
                for(final URL url : urls)
                {
                    System.out.println("Time : " + (System.currentTimeMillis()-this.startTime) + " | Connected to - " + url);
                    final org.jsoup.nodes.Document document = Jsoup.connect(url.toString()).get();
                    final org.jsoup.select.Elements linksOnPage = document.select("a[href]");
                    for(final org.jsoup.nodes.Element element : linksOnPage)
                    {
                        final String urlText = element.attr("abs:href");
                        final URL discoveredURL = new URL(urlText);
                        newURLs.add(discoveredURL);
                    }
                }
            }
            catch (final Exception | Error ignored){

            }
            if(links.size() < 1000)   //No. of URLS to be visited
            {
                Crawl(newURLs);
            }

        }
    }

    private Set<URL> initURLs(URL startURL) {  //returns the singleton set containing startURL
       return Collections.singleton(startURL);
    }

    public static void main(String[] args) throws IOException {
        final WebCrawler crawler = new WebCrawler(new URL("https://en.wikipedia.org/wiki/Cricket"));
    }
}
