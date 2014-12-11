/**
 * This Script is licensed under the MIT license. Use and adapt according to your needs.
 *
 * Disclamer: This script is developed as a way for me to crawl and create static versions of my Ember.js applications.
 * The static versions are then later used in order to provide an SEO friendly version of the website, as well
 * as being able to provide a version of the site to users without JavaScript enabled in their browsers.
 */


/**
 * Helper functions added to the String prototype to check if a String starts or ends with
 * another String.
 */
if (typeof String.prototype.endsWith != 'function') {
    // see below for better implementation!
    String.prototype.endsWith = function (suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
}

if (typeof String.prototype.startsWith != 'function') {
    // see below for better implementation!
    String.prototype.startsWith = function (str) {
        return this.indexOf(str) == 0;
    };
}

(function (host) {

    function Crawler() {
        //A hash of all visited URLs
        this.visitedURLs = {};
        //The remaining URLs to visit
        this.urlsToVisit = [];
        //The intervalId of the main script interval
        this.intervalId = null;
        //The number of pages that are currently processing
        this.currentlyProcessing = 0;
    };

    //We need to both interact with the webpage and the filesystem
    Crawler.webpage = require('webpage');
    var fs = require('fs');

    /**
     * Helper function tht will strip away any <script> and <noscript> tags from a HTML document.
     *
     * @param doc
     * @returns {*}
     */
    Crawler.stripScriptTags = function(doc) {
        if (doc) {
            var SCRIPT_REGEX = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;
            while (SCRIPT_REGEX.test(doc)) {
                doc = doc.replace(SCRIPT_REGEX, "");
            }

            SCRIPT_REGEX = /<noscript\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/noscript>/gi;
            while (SCRIPT_REGEX.test(doc)) {
                doc = doc.replace(SCRIPT_REGEX, "");
            }
        }

        return doc;
    };

    /**
     * Helper function that will replace the occurrence of the find variable with the
     * replace variable in the passed in str variable.
     *
     * Returns the new string after
     *
     * @param find
     * @param replace
     * @param str
     * @returns {*}
     */
    Crawler.replaceAll = function(find, replace, str) {
        if (find && replace && str) {
            return str.replace(new RegExp(find, 'g'), replace);
        }

        return str;
    };

    /**
     * The crawl function will go through all of the URLs that are currently found as part of the website.
     * The Crawler will only fetch 8 pages simultaneously, as to not run out of resources on the system that
     * performs the crawling.
     *
     * Once there are no more URLs to visit, the crawl function will output the final sitemap XML file to the
     * file specified to the script.
     *
     * Some servers are unable to access the public domain of the website, and must crawl via http://localhost:port
     * For these situations, the script will replace any occurance of parameter 5 with parameter 6, which can be used to
     * replace localhost:port with the actual domain name inside the Sitemap.xml file.
     *
     *Once the Sitemap.xml file is written, the script will exit with a status code of 0
     *
     * @param url
     * @param depth
     * @param onSuccess
     * @param onFailure
     */
    Crawler.prototype.crawl = function (url, depth, onSuccess, onFailure) {
        var self = this;
        if (!self.intervalId) {
            self.intervalId = setInterval(function () {
                if (self.currentlyProcessing < 8) {
                    if (self.urlsToVisit.length === 0 && self.currentlyProcessing == 0) {
                        var sitemapContent = '<?xml version="1.0" encoding="UTF-8"?>\n<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" xmlns:image="http://www.google.com/schemas/sitemap-image/1.1" xmlns:video="http://www.google.com/schemas/sitemap-video/1.1">\n';

                        for (var key in self.visitedURLs) {
                            var outputKey = key;

                            if (localurl && replaceurl) {
                                outputKey = Crawler.replaceAll(localurl, replaceurl, key);
                            }
                            outputKey = Crawler.replaceAll("&", "&amp;", outputKey);

                            sitemapContent += '<url><loc>' + outputKey + "</loc></url>\n";
                        }

                        //Write the Sitemap.xml file
                        if (sitemapTarget) {
                            fs.write(sitemapTarget, sitemapContent + '</urlset>', 'w', "utf8");
                        }

                        console.log('Site crawled');
                        phantom.exit(0);

                    } else if (self.urlsToVisit.length > 0) {
                        self.doCrawl(self.urlsToVisit.shift());
                    }
                }
            }, 10);
        }

        if (depth > 0 && !this.isVisited(url) && url.indexOf(urlToCrawl) === 0) {
            self.urlsToVisit.push(url);
        }
    };

    /**
     * This is the function that performs the actual crawling of a URL.
     *
     * It will only crawl one URL once, and it will keep a record of all previously
     * crawled URLs. The function will only crawl URLs that originate from the same
     * domain that is currently being crawled (read: the URL starts with param 1)
     *
     * The function opens the URL, extracts all links adds these links to the list of URLs to crawl
     *
     * The function will also render a static HTML version of the website and write the contents into
     * the directory specified in the scripts third parameter. If the URL ends with a directory, the
     * script will append .html to the file, in order to be able to store nested URLs. You need to handle
     * this fact when you are serving the static content for your webapp.
     *
     * If your URLs use the hash-sign (#), it will also replace all instances of # with the path "/static".
     *
     * The version of the HTML page that is stored is a static version. Any <script> tags are therefore
     * removed from the version written to disk
     * @param url
     */
    Crawler.prototype.doCrawl = function (url) {
        //Only crawl a URL once, and only crawl URLS that originate from the correct domain
        if (!this.visitedURLs[url] && url.indexOf(urlToCrawl) === 0) {
            this.currentlyProcessing++;
            this.visitedURLs[url] = true;

            var self = this;
            var page = Crawler.webpage.create();

            page.open(url, function (status) {
                //console.log('opening page: ' + url);

                if ('success' === status) {
                    setTimeout(function () {

                        //Extract all links on the page
                        var pageUrls = page.evaluate(function () {
                            if (document.body && document.body.innerHTML) {
                                return Array.prototype.slice.call(document.querySelectorAll("a"), 0)
                                    .map(function (link) {
                                        return link.getAttribute("href");
                                    });
                            } else {
                                return [];
                            }
                        });

                        //Extract the static HTML version of the site
                        var documentBody = page.evaluate(function() {
                            if (document.body && document.body.innerHTML) {
                                return document.body.innerHTML;
                            }
                        });

                        var title = page.evaluate(function() {
                            if (document.title) {
                                return document.title;
                            }
                        });

                        console.log('title: ' + title);

                        //Schedule all links to be crawled
                        self.crawlURLs(pageUrls);

                        //Remove the protocol and domain name from the URL (i.e. http://domain.com
                        var pageUrl = url;
                        if (pageUrl.indexOf(urlToCrawl) == 0) {
                            pageUrl = pageUrl.slice(urlToCrawl.length, pageUrl.length);
                        }

                        //Remove /#/ if the URL starts with it
                        if (pageUrl.indexOf("/#/") == 0) {
                            pageUrl = pageUrl.slice(3, pageUrl.length);
                        }

                        //If there is anything left in the URL, store it in the designated target directory, and append .html
                        //Also remove any script tags, so that a true static version will be served
                        if (pageUrl.length > 0) {
                            fs.write(target + "/" + pageUrl + ".html", Crawler.replaceAll('#', '/static', Crawler.stripScriptTags(page.content)), 'w', "utf8");
                        } else {
                            //If there is nothign left in the URL, we are indexing http://domain.com. Store this as index.html
                            fs.write(target + "/index.html", Crawler.replaceAll('#', '/static', Crawler.stripScriptTags(page.content)), 'w', "utf8");
                        }

                    }, maxPageLoad * 1000); //Wait for maxPageLoad seconds before fetching the contents
                }

                //Once the page have been crawled and stored on the filesystem, close the file handle
                setTimeout(function (p) {
                    return function () {
                        //console.log('closing page: ' + url);
                        self.currentlyProcessing--;
                        p.close();
                    };
                }(page), maxPageLoad * 1100); //Wait for just a bit longer than the above timeout before closing the file handle
            });
        }
    };

    /**
     * Helper function that will determine if a URL has already been crawled
     * @param url
     * @returns {boolean}
     */
    Crawler.prototype.isVisited = function (url) {
        var visited = false;

        if (this.visitedURLs[url]) {
            visited = true;
        }

        return visited;
    };

    /**
     * The crawlUrls function will build up the correct URL for the passed in URL. Mostly adding trailing slash-es
     * to ensure that the resst of the script wont have to deal with possible trailing slashes to the URL
     *
     * If the URL has not been visited before, its added to the urlsToVisit array
     *
     * @param urls
     */
    Crawler.prototype.crawlURLs = function (urls) {
        var self = this;
        urls.forEach(function (url) {
            var useUrl = url;

            if (Crawler.isTopLevelURL(url)) {
                useUrl = url;
            } else if (!urlToCrawl.endsWith("/") && !url.startsWith("/")) {
                useUrl = urlToCrawl + "/" + url;
            } else if (urlToCrawl.endsWith("/") || url.startsWith("/")) {
                useUrl = urlToCrawl + url;
            }

            if (!self.visitedURLs[useUrl]) {
                self.urlsToVisit.push(useUrl);
            }
        });
    };

    /**
     * Helper function to check if the URL is absolute or if it is relative to the current path
     * @param url
     * @returns {boolean}
     */
    Crawler.isTopLevelURL = function (url) {
        return 0 == url.indexOf("http");
    };

    host.Crawler = Crawler;
})(phantom);

/*
 The script expects 7 arguments

 Example:

 phantomjs http://localhost:8085 1 /srv/website/static /srv/website/Sitemap.xml localhost:8085 domain.com 6

 The above example will:

 - Crawl the URL http://localhost:8085 (parameter 1) and traverse into any links until it reaches a depth of 6 (parameter 7)
 - The script will wait 1 second (parameter 2) for each page in order to render the full JavaScript. Heavy JS sites might need longer
 - Each of the crawled pages will be stored inside /src/website/static (parameter 3)
 - The final Sitemap.xml file will be stored at /srv/website/Sitemap.xml (parameter 4)
 - When writing the Sitemap.xml, replace any occurence of localhost:8085 (parameter 5) with domain.com (parameter 6)

 */
var args = phantom.args;
if (args.length != 7) {
    console.log('Param 1: URL to crawl\n' +
        'param2: seconds to wait for page load\n' +
        'param3: target dir for static folders\n' +
        'param4: path to Sitemap.xml\n' +
        'param5: part of url to replace (typically localhost:port)\n' +
        'param6: String to replace param5 with\n' +
        'param7: The maximum depth of the crawl');
    phantom.exit(-1);
}

var urlToCrawl = args[0];
var maxPageLoad = args[1];
var target = args[2];
var sitemapTarget = args[3];
var localurl = args[4];
var replaceurl = args[5];
var maxDepth = args[6]

//Start the crawler
var crawler = new phantom.Crawler().crawl(urlToCrawl, maxDepth,
    function onSuccess(page) {
        //console.log(page.url);
    },
    function onFailure(page) {
        //console.log("Could not load page. URL = " +  page.url);
    }
);