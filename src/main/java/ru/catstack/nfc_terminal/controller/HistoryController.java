package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.HistoryService;

@RestController
@RequestMapping("/api/history/")
public class HistoryController {
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/get")
    public ApiResponse getRecords(@RequestParam int from, @RequestParam int count) {
        var records = historyService.getRecordsGap(from, count);
        return new ApiResponse(records.toArray());
    }
}
