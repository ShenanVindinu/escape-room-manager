package com.escaperoom.command;

import com.escaperoom.manager.VenueManager;
import com.escaperoom.stats.StatsService;
import com.escaperoom.cli.ConsoleView;

public class ViewStatsCommand implements Command {

    private final ConsoleView view = new ConsoleView();

    @Override
    public void execute() {
        StatsService stats = new StatsService(VenueManager.getInstance());
        view.showStats(stats.generateReport());
    }

    @Override
    public String getDescription() {
        return "View statistics and reports";
    }
}
