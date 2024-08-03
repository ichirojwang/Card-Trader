package ca.cmpt213.webserver.controllers;

import ca.cmpt213.webserver.models.Tokimon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TokimonController {

    private AtomicLong nextTid;
    private String filename;

    @PostConstruct
    public void init() {
        nextTid = new AtomicLong();
        filename = "webserver/data/tokimoncards.json";
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @GetMapping("/api/tokimon/all")
    public List<Tokimon> getTokimonList() {
        return getTokimonListFromFile();
    }

    @GetMapping("/api/tokimon/{tid}")
    public Tokimon getTokimon(@PathVariable long tid) {

        List<Tokimon> tokimonList = getTokimonListFromFile();

        for (Tokimon tokimon : tokimonList) {
            if (tokimon.getTid() == tid) {
                return tokimon;
            }
        }
        System.out.println("not found");
        return null;
    }

    @PostMapping("/api/tokimon/add")
    public Tokimon addTokimon(@RequestBody Tokimon newTokimon, HttpServletResponse response) {

        List<Tokimon> tokimonList = getTokimonListFromFile();

        newTokimon.setTid(nextTid.getAndIncrement());
        tokimonList.add(newTokimon);
        response.setStatus(HttpServletResponse.SC_CREATED);

        writeToJson(tokimonList);

        return newTokimon;
    }

    @PutMapping("/api/tokimon/edit/{tid}")
    public Tokimon editTokimon(@PathVariable long tid, @RequestBody Tokimon newTokimon) {

        List<Tokimon> tokimonList = getTokimonListFromFile();

        for (Tokimon tokimon : tokimonList) {
            if (tokimon.getTid() == tid) {

                newTokimon.setRarity(newTokimon.getRarity());
                newTokimon.setHp(newTokimon.getHp());

                tokimonList.set(tokimonList.indexOf(tokimon), newTokimon);
                newTokimon.setTid(tid);

                writeToJson(tokimonList);

                return newTokimon;
            }
        }

        System.out.println("not found");
        return null;
    }

    @DeleteMapping("/api/tokimon/{tid}")
    public Tokimon deleteTokimon(@PathVariable long tid, HttpServletResponse response) {

        List<Tokimon> tokimonList = getTokimonListFromFile();

        for (Tokimon tokimon : tokimonList) {
            if (tokimon.getTid() == tid) {
                tokimonList.remove(tokimon);
                writeToJson(tokimonList);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);

                return tokimon;
            }
        }

        System.out.println("not found");
        return null;

    }

    @DeleteMapping("/api/tokimon/all")
    public List<Tokimon> deleteAllTokimon(HttpServletResponse response) {

        List<Tokimon> tokimonList = getTokimonListFromFile();

        try {
            new FileWriter(filename, false).close();
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tokimonList;

    }

    private List<Tokimon> getTokimonListFromFile() {

        List<Tokimon> tokimonList;

        try {

            FileReader fileReader = new FileReader(filename);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Tokimon>>() {}.getType();
            tokimonList = gson.fromJson(fileReader, listType);
            fileReader.close();

            if (tokimonList == null) {
                tokimonList = new ArrayList<>();
                nextTid.set(0);
            }
            else {
                int listSize = tokimonList.size();
                long tid;
                if (listSize == 0) {
                    tid = 0;
                }
                else {
                    tid = tokimonList.get(listSize - 1).getTid() + 1;
                }
                nextTid.set(tid);
            }

            return tokimonList;

        } catch (FileNotFoundException e) {
            System.out.println("Error in opening file");
        } catch (IOException e) {
            System.out.println("Error in closing file");;
        }

        return new ArrayList<>();
    }

    private void writeToJson(List<Tokimon> tokimons) {

        try {
            System.out.println("Writing to json");
            FileWriter writer = new FileWriter(filename, false);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(tokimons, writer);
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to json file");
        }

    }

}
