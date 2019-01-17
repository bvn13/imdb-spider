package ru.bvn13.imdbspider.spider.api.v1_0;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.bvn13.imdbspider.exceptions.processor.HtmlProcessorException;
import ru.bvn13.imdbspider.imdb.Aka;
import ru.bvn13.imdbspider.imdb.AkaDataType;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * Created by bvn13 on 16.01.2019.
 */
public class AkaProcessor_1_0 extends AbstractApiProcessor_1_0<Aka, AkaDataType> {
    public AkaProcessor_1_0(ApiFactory_1_0 apiFactory) {
        super(apiFactory);
    }

    @Override
    Task taskByDataType(AkaDataType dataType, String imdbObjectParentId) {
        Task t = new Task();
        t.setImdbObjectParentId(imdbObjectParentId);
        t.setDataType(dataType);
        switch (dataType) {
            case ID:
                //
                break;
            case NAME:
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    task.setResult("");
                    try {
                        Elements els = getApiFactory().getHtmlProcessor().process(String.format("<table><row>%s</row></table>", task.getSourceHtml()), "td.aka-item__name");
                        if (els.size() > 0) {
                            Element name = els.first();
                            task.setResult(name.text());
                        }
                    } catch (HtmlProcessorException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case TITLE:
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    task.setResult("");
                    try {
                        Elements els = getApiFactory().getHtmlProcessor().process(String.format("<table><row>%s</row></table>", task.getSourceHtml()), "td.aka-item__title");
                        if (els.size() > 0) {
                            Element title = els.first();
                            task.setResult(title.text());
                        }
                    } catch (HtmlProcessorException e) {
                        e.printStackTrace();
                    }
                });
                break;
        }
        return t;
    }

    @Override
    void fillUpImdbObject(Aka aka, Task task) {
        boolean isDone = false;
        switch ((AkaDataType) task.getDataType()) {
            case ID:
                aka.setId((String) task.getResult());
                aka.setUrl(task.getUrl());
                isDone = true;
                break;
            case NAME:
                aka.setName((String) task.getResult());
                isDone = true;
                break;
            case TITLE:
                aka.setTitle((String) task.getResult());
                isDone = true;
                break;
        }

        if (isDone) {
            aka.getRetrievedDataTypes().add((AkaDataType) task.getDataType());
        }
    }
}
