
package springboot.wxcms.service;

import org.springframework.stereotype.Service;
import springboot.wxcms.entity.MsgArticle;
import springboot.wxcms.entity.MsgNews;
import springboot.wxcms.mapper.MsgArticleMapper;
import springboot.wxcms.mapper.MsgNewsMapper;

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
	private MsgArticleMapper msgArticleMapper;
	
	@Resource
	private MsgNewsMapper msgNewsMapper;
	
	public List<MsgArticle> getByNewsId(int id) {
		// TODO Auto-generated method stub
		return msgArticleMapper.getByNewsId(id);
	}

	public MsgArticle getById(String id) {
		// TODO Auto-generated method stub
		return msgArticleMapper.selectByPrimaryKey(id);
	}

	public void add(MsgArticle article) {
		// TODO Auto-generated method stub
		msgArticleMapper.insert(article);
	}

	public void insertByBatch(List<MsgArticle> articles) {
		// TODO Auto-generated method stub
		msgArticleMapper.insertByBatch(articles);
	}

	public void update(MsgArticle article) {
		// TODO Auto-generated method stub
//		this.getById(article.getArId());
		if(article.getNewsIndex()==0){
			MsgNews news = msgNewsMapper.selectByPrimaryKey(String.valueOf(article.getNewsId()));
			news.setTitle(article.getTitle());
			news.setAuthor(article.getAuthor());
			news.setBrief(article.getDigest());
			news.setDescription(article.getContent());
			news.setPicpath(article.getPicUrl());
			news.setThumbMediaId(article.getThumbMediaId());
			news.setFromurl(article.getContentSourceUrl());
			news.setShowpic(article.getShowCoverPic());
			msgNewsMapper.updateByPrimaryKey(news);
		}
		msgArticleMapper.updateByPrimaryKey(article);
	}

	public void delete(String id) {
		// TODO Auto-generated method stub
		msgArticleMapper.deleteByPrimaryKey(id);
	}

	public void deleteByBatch(int id) {
		// TODO Auto-generated method stub
		msgArticleMapper.deleteByBatch(id);
	}

}
