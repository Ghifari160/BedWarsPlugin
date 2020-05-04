package com.ghifari160.bedwarsplugin.ranks;

import java.util.ArrayList;
import java.util.List;

public class RankAttachment
{
    List<Rank> ranks;

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

    public void setRanks(List<Rank> playerRanks)
    {
        ranks = playerRanks;
    }

    public List<Rank> getRanks()
    {
        return ranks;
    }
}
