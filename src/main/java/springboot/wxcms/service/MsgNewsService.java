
package springboot.wxcms.service;


import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.wxapi.process.MsgType;
import springboot.wxcms.entity.MediaFiles;
import springboot.wxcms.entity.MsgArticle;
import springboot.wxcms.entity.MsgBase;
import springboot.wxcms.entity.MsgNews;
import springboot.wxcms.mapper.MediaFilesMapper;
import springboot.wxcms.mapper.MsgArticleMapper;
import springboot.wxcms.mapper.MsgBaseMapper;
import springboot.wxcms.mapper.MsgNewsMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class MsgNewsService {

    @Resource
    private MsgBaseMapper msgBaseMapper;

    @Resource
    private MsgNewsMapper msgNewsMapper;

    @Resource
    private MediaFilesMapper mediaFilesMapper;

    @Resource
    private MsgArticleMapper msgArticleMapper;

    public MsgNews getById(String id) {
        return msgNewsMapper.selectByPrimaryKey(id);
    }

    public List<MsgNews> listForPage(MsgNews searchEntity) {
        PageHelper.startPage(searchEntity.getPageNum(),searchEntity.getPageSize());
        return msgNewsMapper.listForPage(searchEntity);
    }

    public List<MsgNews> getWebNewsListByPage(MsgNews searchEntity) {
        return msgNewsMapper.getWebNewsListByPage(searchEntity);
    }


    public void add(MsgNews entity) {

        MsgBase base = new MsgBase();
        base.setInputcode(entity.getInputcode());
        base.setCreateTime(LocalDateTime.now());
        base.setMsgtype(MsgType.News.toString());
        msgBaseMapper.insert(base);

        entity.setBaseId(base.getId());
        msgNewsMapper.insert(entity);

        if (StringUtils.isEmpty(entity.getFromurl())) {
            entity.setUrl(entity.getUrl() + "?id=" + entity.getId());
        } else {
            entity.setUrl("");
        }

        msgNewsMapper.updateUrl(entity);
    }

    public void update(MsgNews entity) {
        MsgBase base = msgBaseMapper.selectByPrimaryKey(entity.getBaseId());
        base.setInputcode(entity.getInputcode());
        msgBaseMapper.updateInputcode(base);

        if (StringUtils.isEmpty(entity.getFromurl())) {
            entity.setUrl(entity.getUrl() + "?id=" + entity.getId());
        } else {
            entity.setUrl("");
        }

        msgNewsMapper.updateByPrimaryKey(entity);
    }

    public void delete(MsgNews entity) {
        MsgBase base = new MsgBase();
        base.setId(entity.getBaseId());
        msgArticleMapper.deleteByBatch(entity.getId());
        msgNewsMapper.deleteByPrimaryKey(entity.getId());
        msgBaseMapper.deleteByPrimaryKey(entity.getId());

    }

    public List<MsgNews> getRandomMsg(String inputCode, Integer num) {
        return msgNewsMapper.getRandomMsgByContent(inputCode, num);
    }

    public MsgNews getByBaseId(String baseid) {
        return msgNewsMapper.getByBaseId(baseid);
    }


    public int updateMediaId(MsgNews entity) {
        int n = 0;
        try {
            msgNewsMapper.updateMediaId(entity);
            n = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }


    @Transactional
    public int addSingleNews(MsgNews news, MediaFiles entity) {
        int n = 0;
        int m = 0;
        try {
            //保存基本消息
            MsgBase base = new MsgBase();
            base.setCreateTime(LocalDateTime.now());
            base.setMsgtype(MsgType.News.toString());
            msgBaseMapper.insert(base);
            //保存图文信息
            news.setCreateTime(LocalDateTime.now());
            news.setBaseId(base.getId());
            Integer newId = this.msgNewsMapper.insert(news);
            MsgArticle art = new MsgArticle();
            art.setAuthor(news.getAuthor());
            art.setContent(news.getDescription());
            art.setContentSourceUrl(news.getFromurl());
            art.setDigest(news.getBrief());
            art.setMediaId(news.getMediaId());
            art.setNewsIndex(0);
            art.setPicUrl(news.getPicpath());
            art.setShowCoverPic(news.getShowpic());
            art.setThumbMediaId(news.getThumbMediaId());
            art.setTitle(news.getTitle());
            art.setUrl(news.getUrl());

            art.setNewsId(news.getId());
            msgArticleMapper.insert(art);
            n = 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (n > 0) {
            try {
                mediaFilesMapper.insert(entity);
                m = 1;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        if (n > 0 && m > 0) {
            return 1;
        } else {
            return 0;
        }
    }


    public int addMediaFiles(MediaFiles entity) {
        int n = 0;

        try {
            mediaFilesMapper.insert(entity);
            n = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (n > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    /* (non-Javadoc)
     * @see com.wxmp.wxcms.service.MsgNewsService#getMediaFileList()
     */
    public List<MediaFiles> getMediaFileList() {
        return this.mediaFilesMapper.getMediaFileList();
    }

    /* (non-Javadoc)
     * @see com.wxmp.wxcms.service.MsgNewsService#getMsgNewsList()
     */
    public List<MsgNews> getMsgNewsList() {
        return this.msgNewsMapper.getMsgNewsList();
    }

    /* (non-Javadoc)
     * @see com.wxmp.wxcms.service.MsgNewsService#addMoreNews(com.wxmp.wxcms.domain.MsgNews)
     */
    @Transactional
    public int addMoreNews(MsgNews news) {
        int n = 0;

        try {
            List<MsgArticle> articles = news.getArticles();
            List<MsgArticle> list = new ArrayList<MsgArticle>();
            //保存基本消息
            MsgBase base = new MsgBase();
            base.setCreateTime(LocalDateTime.now());
            base.setMsgtype(MsgType.News.toString());
            msgBaseMapper.insert(base);
            news.setBaseId(base.getId());
            news.setCreateTime(LocalDateTime.now());
            //保存图文信息
            this.msgNewsMapper.insert(news);
            for (int i = 0; i < articles.size(); i++) {
                MsgArticle article = articles.get(i);
                article.setNewsId(news.getId());
                list.add(article);
            }
            msgArticleMapper.insertByBatch(list);
            n = 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (n == 1) {
            MediaFiles entity = new MediaFiles();
            entity.setMediaId(news.getMediaId());
            entity.setMediaType("news");
            entity.setCreateTime(news.getCreateTime());
            entity.setUpdateTime(news.getCreateTime());
            mediaFilesMapper.insert(entity);
        }
        return n;
    }

    public Boolean addMoreNews(List<MsgNews> news) {
        // TODO Auto-generated method stub
        try {
            //保存基本消息
            MsgBase base = new MsgBase();
            base.setCreateTime(LocalDateTime.now());
            base.setMsgtype(MsgType.News.toString());
            msgBaseMapper.insert(base);
            for (int i = 0; i < news.size(); i++) {
                MsgNews one = news.get(i);
                one.setBaseId(base.getId());
                one.setCreateTime(LocalDateTime.now());
                //保存图文信息
                this.msgNewsMapper.insert(one);
            }
            //添加到素材表中
            MediaFiles entity = new MediaFiles();
            entity.setMediaId(news.get(0).getMediaId());
            entity.setMediaType("news");
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            mediaFilesMapper.insert(entity);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void deleteNews(String mediaId) {
        this.mediaFilesMapper.deleteByMediaId(mediaId);
        this.msgNewsMapper.deleteByMediaId(mediaId);
    }

    /* (non-Javadoc)修改单图文
     */
    @Transactional
    public void updateSingleNews(MsgNews news) {
        MsgArticle art = new MsgArticle();
        art.setAuthor(news.getAuthor());
        art.setContent(news.getDescription());
        art.setContentSourceUrl(news.getFromurl());
        art.setDigest(news.getBrief());
        art.setMediaId(news.getMediaId());
        art.setNewsIndex(0);
        art.setPicUrl(news.getPicpath());
        art.setShowCoverPic(news.getShowpic());
        art.setThumbMediaId(news.getThumbMediaId());
        art.setTitle(news.getTitle());
        art.setUrl(news.getUrl());

        art.setNewsId(news.getId());
        int arId = msgArticleMapper.getByNewsId(news.getId()).get(0).getArId();
        art.setArId(arId);
        msgArticleMapper.updateByPrimaryKey(art);
        this.msgNewsMapper.updateByPrimaryKey(news);
    }

    public List<MsgNews> getByMediaId(String mediaId) {
        // TODO Auto-generated method stub
        return this.msgNewsMapper.getByMediaId(mediaId);
    }


}
