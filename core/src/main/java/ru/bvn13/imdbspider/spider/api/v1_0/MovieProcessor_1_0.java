package ru.bvn13.imdbspider.spider.api.v1_0;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.bvn13.imdbspider.exceptions.processor.HtmlProcessorException;
import ru.bvn13.imdbspider.imdb.Movie;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.imdb.TaglineListDataType;
import ru.bvn13.imdbspider.imdb.accessories.Link;
import ru.bvn13.imdbspider.imdb.accessories.SoundMix;
import ru.bvn13.imdbspider.spider.tasker.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author boyko_vn at 15.01.2019
 */
public class MovieProcessor_1_0 extends AbstractApiProcessor_1_0 {

    public MovieProcessor_1_0(ApiFactory_1_0 apiFactory) {
        super(apiFactory);
    }

    public Task taskByMovieDataType(MovieDataType movieDataType) {
        Task t = new Task();
        t.setDataType(movieDataType);
        switch (movieDataType) {
            case ID:
                t.setPostprocess((task, s) -> {
                    Matcher matcher = ApiFactory_1_0.PATTERN_MOVIE_ID_FROM_MOVIELIST.matcher(task.getUrl());
                    if (matcher.find()) {
                        task.setResultType(String.class);
                        task.setResult(matcher.group(1));
                    }
                });
                break;
            case TITLE:
                t.setCssSelector("#title-overview-widget > div.vital > div.title_block > div > div.titleBar > div.title_wrapper > h1");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.GET_WHOLE_TEXT_OF_FIRST_ELEMENT);
                break;
            case ORIGINAL_TITLE:
                t.setCssSelector("#title-overview-widget > div.vital > div.title_block > div > div.titleBar > div.title_wrapper > div.originalTitle");
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    task.setResult("");
                    if (task.getCssSelectorResult().size() > 0) {
                        task.setResult(task.getCssSelectorResult().first().text());
                    } else {
                        try {
                            Elements titles = getApiFactory().getHtmlProcessor().process(s, "#title-overview-widget > div.vital > div.title_block > div > div.titleBar > div.title_wrapper > h1"); // like title
                            if (titles.size() > 0) {
                                task.setResult(titles.first().ownText());
                            }
                        } catch (HtmlProcessorException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case YEAR:
                t.setCssSelector("#titleYear > a");
                t.setPostprocess((task, s) -> {
                    task.setResultType(Integer.class);
                    if (task.getCssSelectorResult().size() > 0) {
                        try {
                            task.setResult(Integer.parseInt(task.getCssSelectorResult().first().text().trim()));
                        } catch (NumberFormatException e) {
                            task.setResult(-1);
                        }
                    } else {
                        task.setResult(-1);
                    }
                });
                break;
            case POSTER:
                t.setCssSelector("#title-overview-widget > div.vital > div.slate_wrapper > div.poster > a > img");
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    if (task.getCssSelectorResult().size() > 0) {
                        task.setResult(task.getCssSelectorResult().first().attr("src"));
                    } else {
                        task.setResult("");
                    }
                });
                break;
            case STORYLINE:
                t.setCssSelector("#titleStoryLine > div:nth-child(3) > p > span");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.GET_TEXT_OF_FIRST_ELEMENT);
                break;
            case RANDOM_TAGLINE:
                t.setCssSelector("#titleStoryLine > div > h4:contains(Taglines)"); //#titleStoryLine > div:nth-child(8) > h4
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case GENRES:
                t.setCssSelector("#titleStoryLine > div > h4:contains(Genres)");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.COLLECT_TITLES_OF_ALL_NESTED_LINKS_OF_PARENT_NODE);
                break;
            case CERTIFICATE:
                t.setCssSelector("#titleStoryLine > div > h4:contains(Certificate)");
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    if (task.getCssSelectorResult().size() > 0) {
                        task.setResult(task.getCssSelectorResult().first().parent().select("span:nth-child(2)").first().text().trim());
                    }
                });
                break;
            case OFFICIAL_SITES:
                t.setCssSelector("#titleDetails > div > h4:contains(Official Sites)");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.COLLECT_ALL_NESTED_LINKS_OF_PARENT_NODE);
                break;
            case COUNTRIES:
                t.setCssSelector("#titleDetails > div > h4:contains(Country)");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.COLLECT_TITLES_OF_ALL_NESTED_LINKS_OF_PARENT_NODE);
                break;
            case LANGUAGES:
                t.setCssSelector("#titleDetails > div > h4:contains(Language)");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.COLLECT_TITLES_OF_ALL_NESTED_LINKS_OF_PARENT_NODE);
                break;
            case RELEASE_DATE:
                t.setCssSelector("#titleDetails > div > h4:contains(Release Date)");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case BUDGET:
                t.setCssSelector("#titleDetails > div > h4:contains(Budget)");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case CUMULATIVE_WORLDWIDE_GROSS:
                t.setCssSelector("#titleDetails > div > h4:contains(Cumulative Worldwide Gross)");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case RUNTIME:
                t.setCssSelector("#titleDetails > div > h4:contains(Runtime)");
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    task.setResult("");
                    if (task.getCssSelectorResult().size() > 0) {
                        task.setResult(task.getCssSelectorResult().first().parent().text().replace("Runtime:", "").trim());
                    }
                });
                break;
            case SOUND_MIXES:
                t.setCssSelector("#titleDetails > div > h4:contains(Sound Mix)");
                t.setPostprocess((task, s) -> {
                    task.setResultType(List.class);
                    List<SoundMix> titles = new ArrayList<>();
                    if (task.getCssSelectorResult().size() > 0) {
                        String html = task.getCssSelectorResult().first().parent().html();
                        html = html.replace("\r", "");
                        html = html.replace("\n", "");
                        html = html.replace("<span class=\"ghost\">|</span>", "|");

                        // remove header: <h4 class="inline">Sound Mix:</h4>
                        html = html.replaceAll("(<h4.+\\/h4>)", "");

                        String[] lines = html.split("\\|");

                        for (int i=0; i<lines.length; i++) {
                            try {
                                Elements els = getApiFactory().getHtmlProcessor().process(String.format("<div>%s</div>", lines[i]), "div");
                                if (els.size() > 0) {
                                    Element div = els.first();
                                    Element link = div.selectFirst("a");
                                    titles.add(new SoundMix()
                                            .setName(link.text().trim())
                                            .setDescription(div.ownText())
                                    );
                                }
                            } catch (HtmlProcessorException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    task.setResult(titles);
                });
                break;
            case COLOR:
                t.setCssSelector("#titleDetails > div > h4:contains(Color)");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.GET_TITLE_OF_FIRST_LINK_IN_PARENT_MODE);
                break;
            case ASPECT_RATIO:
                t.setCssSelector("#titleDetails > div > h4:contains(Aspect Ratio)");
                t.setPostprocess(ApiFactory_1_0.POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case TAGLINES:
                t.setCssSelector("#titleStoryLine > div > h4:contains(Taglines)");
                t.setPostprocess((task, s) -> {
                    if (task.getCssSelectorResult().size() > 0) {
                        Elements links = task.getCssSelectorResult().first().parent().select("span > a:contains(See more)");
                        if (links.size() > 0) {
                            Task newTask = getApiFactory().getTaglineListProcessor().taskByTaglineListDataType(TaglineListDataType.ELEMENTS)
                                    .setParentTask(task)
                                    .setUrl(String.format("%s%s", ApiFactory_1_0.URL_MAIN, links.first().attr("href")));
                            task.getNestedTasks().add(newTask);
                        }
                    }
                });
                break;
        }
        return t;
    }


    void fillUpMovie(Movie movie, Task task) {
        boolean isDone = false;
        switch ((MovieDataType) task.getDataType()) {
            case ID:
                if (isDebug) {
                    movie.setHtml(task.getHtml());
                }
                movie.setUrl(task.getUrl());
                movie.setId((String) task.getResult());
                isDone = true;
                break;
            case TITLE:
                movie.setTitle((String) task.getResult());
                isDone = true;
                break;
            case ORIGINAL_TITLE:
                movie.setOriginalTitle((String) task.getResult());
                isDone = true;
                break;
            case YEAR:
                movie.setYear((Integer) task.getResult());
                isDone = true;
                break;
            case POSTER:
                movie.setPosterLink((String) task.getResult());
                isDone = true;
                break;
            case STORYLINE:
                movie.setStoryline((String) task.getResult());
                isDone = true;
                break;
            case RANDOM_TAGLINE:
                movie.setRandomTagline((String) task.getResult());
                isDone = true;
                break;
            case GENRES:
                movie.setGenres((List<String>) task.getResult());
                isDone = true;
                break;
            case CERTIFICATE:
                movie.setCertificate((String) task.getResult());
                isDone = true;
                break;
            case OFFICIAL_SITES:
                movie.setOfficialSites((List<Link>) task.getResult());
                isDone = true;
                break;
            case COUNTRIES:
                movie.setCountries((List<String>) task.getResult());
                isDone = true;
                break;
            case LANGUAGES:
                movie.setLanguages((List<String>) task.getResult());
                isDone = true;
                break;
            case RELEASE_DATE:
                movie.setReleaseDate((String) task.getResult());
                isDone = true;
                break;
            case BUDGET:
                movie.setBudget((String) task.getResult());
                isDone = true;
                break;
            case CUMULATIVE_WORLDWIDE_GROSS:
                movie.setCumulativeWorldwideGross((String) task.getResult());
                isDone = true;
                break;
            case RUNTIME:
                movie.setRuntime((String) task.getResult());
                isDone = true;
                break;
            case SOUND_MIXES:
                movie.setSoundMixes((List<SoundMix>) task.getResult());
                isDone = true;
                break;
            case COLOR:
                movie.setColor((String) task.getResult());
                isDone = true;
                break;
            case ASPECT_RATIO:
                movie.setAspectRatio((String) task.getResult());
                isDone = true;
                break;
            case TAGLINES:
                isDone = true;
        }

        if (isDone) {
            movie.getRetrievedDataTypes().add((MovieDataType) task.getDataType());
        }
    }


}
