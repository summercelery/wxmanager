
package springboot.wxcms.service;

import org.springframework.stereotype.Service;
import springboot.wxcms.entity.MsgArticle;
import springboot.wxcms.entity.MsgNews;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author hermit
 * @version 2.0
 * @date 2018-04-17 10:54:58
 */
@Service
public class MsgArticleService {

	@Resource
	private MsgArticleDao articleDao;
	
	@Resource
	private MsgNewsDao entityDao;
	
	@Override
	public List<MsgArticle> getByNewsId(int id) {
		// TODO Auto-generated method stub
		return articleDao.getByNewsId(id);
	}

	@Override
	public MsgArticle getById(int id) {
		// TODO Auto-generated method stub
		return articleDao.getById(id);
	}

	@Override
	public void add(MsgArticle article) {
		// TODO Auto-generated method stub
		articleDao.add(article);
	}

	@Override
	public void insertByBatch(List<MsgArticle> articles) {
		// TODO Auto-generated method stub
		articleDao.insertByBatch(articles);
	}

	@Override
	public void update(MsgArticle article) {
		// TODO Auto-generated method stub
//		this.getById(article.getArId());
		if(article.getNewsIndex()==0){
			MsgNews news = entityDao.getById(String.valueOf(article.getNewsId()));
			news.setTitle(article.getTitle());
			news.setAuthor(article.getAuthor());
			news.setBrief(article.getDigest());
			news.setDescription(article.getContent());
			news.setPicpath(article.getPicUrl());
			news.setThumbMediaId(article.getThumbMediaId());
			news.setFromurl(article.getContentSourceUrl());
			news.setShowpic(article.getShowCoverPic());
			entityDao.update(news);
		}
		articleDao.update(article);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		articleDao.delete(id);
	}

	@Override
	public void deleteByBatch(int id) {
		// TODO Auto-generated method stub
		articleDao.deleteByBatch(id);
	}

}
