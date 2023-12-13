package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.AnnotationDao;
import com.buaa.markpp.dao.BlockDao;
import com.buaa.markpp.entity.Block;
import com.buaa.markpp.model.BlockTree;
import com.buaa.markpp.service.BlockService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BlockServiceImpl implements BlockService {
    private final BlockDao blockDao;
    private final AnnotationDao annotationDao;

    public BlockServiceImpl(BlockDao blockDao, AnnotationDao annotationDao) {
        this.blockDao = blockDao;
        this.annotationDao = annotationDao;
    }

    @Override
    public void addBlocks(int postId, BlockTree node, String content) {
        Block block = new Block();
        block.setContent(content.substring(node.getStartOffSet(), node.getEndOffSet()));
        block.setPostId(postId);
        if (node.getFirstSon() != null) {
            addBlocks(postId, node.getFirstSon(), content);
            block.setFirstSonId(node.getFirstSon().getBlockId());
        }
        if (node.getNextBrother() != null) {
            addBlocks(postId, node.getNextBrother(), content);
            block.setNextBrotherId(node.getNextBrother().getBlockId());
        }
        blockDao.insert(block);
        node.setBlockId(block.getBlockId());
    }

    /**
     * Delete blocks and its annotations together
     */
    private void deleteBlockTree(BlockTree root) {
        if (root.getFirstSon() != null)
            deleteBlockTree(root.getFirstSon());
        if (root.getNextBrother() != null)
            deleteBlockTree(root.getNextBrother());
        if (root.getNextBrother() == null && root.getFirstSon() == null)
            blockDao.deleteByBlockId(root.getBlockId());
    }

    private void blockTreeBuild(Block block, BlockTree root) {
        String blockContent = block.getContent();
        String pattern = "^#{1,3} .*";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(blockContent);
        String heading = null;
        int level = 0;
        if (matcher.find()) {
            String headingStr = matcher.group(0);
            level = headingStr.indexOf(' ');
            heading = headingStr.substring(level + 1);
        }

        root.setBlockId(block.getBlockId());
        root.setLevel(level);
        root.setHeading(heading);

        if (block.getFirstSonId() > 0) {
            BlockTree sonNode = new BlockTree();
            sonNode.setFather(root);
            root.setFirstSon(sonNode);
            Block son = blockDao.select(block.getFirstSonId());
            blockTreeBuild(son, sonNode);
        }
        if (block.getNextBrotherId() > 0) {
            BlockTree broNode = new BlockTree();
            broNode.setFather(root.getFather());
            root.setNextBrother(broNode);
            Block brother = blockDao.select(block.getNextBrotherId());
            blockTreeBuild(brother, broNode);
        }
    }

    private void compare(BlockTree newTree, BlockTree originalTree) {
        // 对准根节点
        BlockTree node = BlockTree.find(newTree.getHeading(), newTree.getLevel(), originalTree);
        if (node != null) {
            if (newTree.getFirstSon() != null) {
                compare(newTree.getFirstSon(), node);
            }
            annotationDao.replaceBlockId(newTree.getBlockId(), node.getBlockId());
        }
        if (newTree.getNextBrother() != null) {
            if (originalTree.getFather() == null)
                compare(newTree.getNextBrother(), originalTree);
            else
                compare(newTree.getNextBrother(), originalTree.getFather());
        }
    }

    /**
     * 比对更新 Blocks<br>
     * 新的节点与原先的节点满足条件：<br>
     *  1. 标题和等级相同<br>
     *  2. 父节点标题和等级相同<br>
     * 就视为同一节点<br>
     * 将原先节点的批注移动到满足条件的新节点上，
     * 删除所有原先的节点及所拥有的批注
     */
    @Override
    public void modifyBlocks(int postId, BlockTree node, String content, boolean annotationChg) {
        Block block = blockDao.selectBlockRoot(postId);
        addBlocks(block.getPostId(), node, content);
        BlockTree oldRoot = new BlockTree();
        if (!annotationChg) {
            blockTreeBuild(block, oldRoot);
            compare(node, oldRoot);
        }
        deleteBlockTree(oldRoot);
    }

    @Override
    public Block getBlock(int blockId) {
        return blockDao.select(blockId);
    }

    private int blockListBuild(List<Block> blocks, Block block, int index) {
        blocks.add(block);
        if (block.getFirstSonId() > 0) {
            index ++;
            Block son = blockDao.select(block.getFirstSonId());
            block.setFirstSonId(index);
            index = blockListBuild(blocks, son, index);
        } else {
            block.setFirstSonId(-1);
        }
        if (block.getNextBrotherId() > 0) {
            index ++;
            Block brother = blockDao.select(block.getNextBrotherId());
            block.setNextBrotherId(index);
            index = blockListBuild(blocks, brother, index);
        } else {
            block.setNextBrotherId(-1);
        }
        return index;
    }

    @Override
    public List<Block> getBlocks(int postId) {
        List<Block> blocks = new ArrayList<>();
        Block block = blockDao.selectBlockRoot(postId);
        if (block != null) {
            blockListBuild(blocks, block, 0);
        }
        return blocks;
    }
}
