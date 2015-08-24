package com.dumptruckman.dircbot.util;

import com.dumptruckman.dircbot.DIRCBot;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DiceCache {

    private static final int QUEUE_SIZE = 200;
    private static final int REFILL_LIMIT = 101;
    private static final String RANDOM_ORG_URL = "https://www.random.org/integers/?num=%s&min=%s&max=%s&col=1&base=10&format=plain&rnd=new";

    private final DIRCBot bot;
    private final ExecutorService executorService;
    private final Random backupRandom = new Random();

    private Future lastRefillRequest = null;

    private final Queue<Integer> d2Cache = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private final Queue<Integer> d3Cache = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private final Queue<Integer> d4Cache = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private final Queue<Integer> d6Cache = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private final Queue<Integer> d8Cache = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private final Queue<Integer> d10Cache = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private final Queue<Integer> d12Cache = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private final Queue<Integer> d20Cache = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private final Queue<Integer> d100Cache = new LinkedBlockingQueue<>(QUEUE_SIZE);

    public DiceCache(@NotNull DIRCBot bot) {
        this.bot = bot;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public Future refillCache() {
        if (lastRefillRequest != null && !lastRefillRequest.isDone() && !lastRefillRequest.isCancelled()) {
            bot.log("Cache refill requested but currently waiting for previous job to complete.");
            return lastRefillRequest;
        }
        lastRefillRequest = executorService.submit(new CacheRefiller());
        return lastRefillRequest;
    }

    public void checkCache() {
        if (d10Cache.size() < REFILL_LIMIT) {
            refillCache();
        }
    }

    private class CacheRefiller implements Runnable {
        @Override
        public void run() {
            getNewRolls(d2Cache, 2);
            getNewRolls(d3Cache, 3);
            getNewRolls(d4Cache, 4);
            getNewRolls(d6Cache, 6);
            getNewRolls(d8Cache, 8);
            getNewRolls(d10Cache, 10);
            getNewRolls(d12Cache, 12);
            getNewRolls(d20Cache, 20);
            getNewRolls(d100Cache, 100);
        }

        private void getNewRolls(Queue<Integer> rolls, int sides) {
            int number = QUEUE_SIZE - rolls.size();
            if (number <= 0) {
                bot.log("Not fetching " + number + " d" + sides + "s!");
            } else {
                bot.log("Fetching " + number + " d" + sides + "s...");
                List<Integer> newRolls = getRandomInts(number, 1, sides);
                if (rolls.addAll(newRolls)) {
                    bot.log("Added " + newRolls.size() + " new d" + sides + "s.");
                } else {
                    bot.log("Unable to add an extra " + newRolls.size() + " new d" + sides + "s...");
                }
            }
        }
    }

    private List<Integer> getRandomInts(int number, int min, int max) {
        List<Integer> randoms = new ArrayList<>(number);

        URL randomOrg;
        try {
            randomOrg = new URL(String.format(RANDOM_ORG_URL, number, min, max));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return randoms;
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(randomOrg.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                try {
                    Integer randomInt = Integer.parseInt(inputLine);
                    randoms.add(randomInt);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return randoms;
    }

    public int getRoll(int sides) {
        switch (sides) {
            case 2:
                return getRoll(d2Cache, 2);
            case 3:
                return getRoll(d3Cache, 3);
            case 4:
                return getRoll(d4Cache, 4);
            case 6:
                return getRoll(d6Cache, 6);
            case 8:
                return getRoll(d8Cache, 8);
            case 10:
                return getRoll(d10Cache, 10);
            case 12:
                return getRoll(d12Cache, 12);
            case 20:
                return getRoll(d20Cache, 20);
            case 100:
                return getRoll(d100Cache, 100);
            default:
                return backupRandom.nextInt(sides) + 1;
        }
    }

    private int getRoll(Queue<Integer> rolls, int sides) {
        if (rolls.isEmpty()) {
            try {
                refillCache().get(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return backupDice(sides);
            } catch (ExecutionException e) {
                e.printStackTrace();
                return backupDice(sides);
            } catch (TimeoutException e) {
                bot.log("Returning backup random due to >2 second wait...");
                return backupDice(sides);
            }
        }
        Integer roll = rolls.poll();
        checkCache();
        if (roll == null) {
            return backupDice(sides);
        } else {
            return roll;
        }
    }

    private int backupDice(int sides) {
        return backupRandom.nextInt(sides) + 1;
    }
}
