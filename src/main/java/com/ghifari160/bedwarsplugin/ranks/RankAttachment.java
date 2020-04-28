package com.ghifari160.bedwarsplugin.ranks;

import java.util.ArrayList;
import java.util.List;

public class RankAttachment
{
    final List<Rank> ranks;

    public RankAttachment()
    {
        ranks = new ArrayList<Rank>();
    }

    public void addRank(Rank rank)
    {
        ranks.add(rank);
    }

    public void removeRank(Rank rank)
    {
        ranks.remove(rank);
    }

    public List<Rank> getRanks()
    {
        return ranks;
    }
}
