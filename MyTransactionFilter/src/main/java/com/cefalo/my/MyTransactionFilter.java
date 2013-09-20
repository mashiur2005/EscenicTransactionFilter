package com.cefalo.my;

import neo.xredsys.api.Article;
import neo.xredsys.api.ArticleTransaction;
import neo.xredsys.api.FilterException;
import neo.xredsys.api.IOTransaction;
import neo.xredsys.api.services.TransactionFilterService;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: escenic
 * Date: 9/19/13
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyTransactionFilter extends TransactionFilterService {
    private final Logger logger = Logger.getLogger(getClass());

    private String fieldToFactorial;
    private String fieldToUpdate;

    public MyTransactionFilter() {

    }

    public String getFieldToFactorial() {
        return fieldToFactorial;
    }

    public void setFieldToFactorial(String fieldToFactorial) {
        this.fieldToFactorial = fieldToFactorial;
    }

    public String getFieldToUpdate() {
        return fieldToUpdate;
    }

    public void setFieldToUpdate(String fieldToUpdate) {
        this.fieldToUpdate = fieldToUpdate;
    }

    @Override
    public void doCreate(IOTransaction pTransaction) throws FilterException {
        super.doCreate(pTransaction);
        logger.debug("In the doCreate.................");
        if (pTransaction instanceof ArticleTransaction) {
            countFactorial((ArticleTransaction) pTransaction);
        }
        relatedArticles(pTransaction);
    }

    @Override
    public void doDelete(IOTransaction pTransaction) throws FilterException {
        super.doDelete(pTransaction);
        logger.debug("In the doDelete.................");
    }

    @Override
    public void doUpdate(IOTransaction pTransaction) throws FilterException {
        super.doUpdate(pTransaction);
        logger.debug("In the doUpdate.................");
        if (pTransaction instanceof ArticleTransaction) {
            countFactorial((ArticleTransaction) pTransaction);
        }
        relatedArticles(pTransaction);
    }

    @Override
    public void doStagedUpdate(IOTransaction pTransaction) throws FilterException {
        super.doStagedUpdate(pTransaction);
        logger.debug("In the doStageUpdate............");
    }

    @Override
    public void doStagedDelete(IOTransaction pTransaction) throws FilterException {
        super.doStagedDelete(pTransaction);
        logger.debug("In the doStagedDelete...........");
    }

    private void countFactorial(ArticleTransaction pArticle) {
        if (pArticle.getType().getName().toLowerCase().equals("news")) {
            BigDecimal original = (BigDecimal) pArticle.getFieldValue(getFieldToFactorial());
            int counter = original.intValue();
            int sum = 1;
            while (counter > 0) {
                sum *= counter;
                counter--;
            }
            pArticle.setFieldValue(getFieldToUpdate(), (Integer) sum);
        }
    }

    private void relatedArticles(IOTransaction ioTransaction) {
        ArticleTransaction articleTransaction = (ArticleTransaction) ioTransaction;
        for (Article apiArticle : articleTransaction.getRelatedAPIArticles()) {
            logger.debug(String.format("Api Articles are %s...........................", apiArticle.getType().getName()));
        }
    }
}
