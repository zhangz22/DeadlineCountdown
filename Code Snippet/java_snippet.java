import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler { 

    public static void main(String[] args) { 

        // timeout connection after 500 miliseconds
        System.setProperty("sun.net.client.defaultConnectTimeout", "500");
        System.setProperty("sun.net.client.defaultReadTimeout",    "1000");

        // initial web page
        String s = args[0];

        // list of web pages to be examined
        Queue<String> queue = new Queue<String>();
        queue.enqueue(s);

        // set of examined web pages
        SET<String> marked = new SET<String>();
        marked.add(s);

        // breadth first search crawl of web
        while (!queue.isEmpty()) {
            String v = queue.dequeue();
            StdOut.println(v);

            String input = null;
            try {
                In in = new In(v);
                input = in.readAll().toLowerCase();
            }
            catch (IllegalArgumentException e) {
                StdOut.println("[could not open " + v + "]");
                continue;
            }

            // if (input == null) continue;


           /*************************************************************
            *  Find links of the form: http://xxx.yyy.com
            *  \\w+ for one or more alpha-numeric characters
            *  \\. for dot
            *  could take first two statements out of loop
            *************************************************************/
            String regexp = "(http|https)://(\\w+\\.)+(edu|com|gov|org)";
            Pattern pattern = Pattern.compile(regexp);

            Matcher matcher = pattern.matcher(input);

            // find and print all matches
            while (matcher.find()) {
                String w = matcher.group();
                if (!marked.contains(w)) {
                    queue.enqueue(w);
                    marked.add(w);
                }
            }

        }
    }
}
