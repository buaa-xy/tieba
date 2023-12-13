package com.buaa.markpp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockTree {
    private int blockId;

    private String heading;

    private int startOffSet;

    private int endOffSet;

    private int level;

    private BlockTree father;

    private BlockTree firstSon;

    private BlockTree nextBrother;

    private BlockTree LastBrother;

    public BlockTree getRoot() {
        if (this.getFather() == null)
            return this.getBrother();
        return this.father.getRoot();
    }

    public BlockTree getBrother() {
        if (this.getLastBrother() == null)
            return this;
        return this.LastBrother.getBrother();
    }

    static public BlockTree find(String heading, int level, BlockTree root) {
        if (root.getLevel() > level)
            return null;
        else if (root.getLevel() == level) {
            if (heading == null)
                return root;
            else if (heading.equals(root.getHeading()))
                return root;
            else {
                if (root.getNextBrother() != null)
                    return find(heading, level, root.getNextBrother());
                return null;
            }
        } else {
            BlockTree result = null;
            BlockTree sonNode = root.getFirstSon();
            while (sonNode != null && result == null) {
                result = find(heading, level, sonNode);
                if (result == null)
                    sonNode = sonNode.getNextBrother();
            }
            return result;
        }
    }
}
