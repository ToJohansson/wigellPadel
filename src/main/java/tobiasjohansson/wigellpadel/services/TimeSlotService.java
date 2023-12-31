package tobiasjohansson.wigellpadel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tobiasjohansson.wigellpadel.exceptions.ResourceNotFoundException;
import tobiasjohansson.wigellpadel.logging.Log4j;
import tobiasjohansson.wigellpadel.models.TimeSlot;
import tobiasjohansson.wigellpadel.repositories.TimeSlotRepository;

import java.util.List;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private CurrencyConversionService currencyConversionService;

    public TimeSlotService() {
    }

    public List<TimeSlot> getCourts() {
        return timeSlotRepository.findAll();
    }

    public TimeSlot getTimeSlotById(long id) throws ResourceNotFoundException {
        return timeSlotRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TimeSlot", "ID", id));
    }
    public void saveTimeSlot(TimeSlot timeSlot){
        timeSlot.setEuro( currencyConversionService.convertSEKToEUR(timeSlot.getSek()));
        timeSlotRepository.save(timeSlot);
    }

    // UPDATE information
    public TimeSlot updateTimeSlot(TimeSlot updateTimeSlot){
        long id = updateTimeSlot.getTimeSlotId();
        TimeSlot existingTimeSlot = getTimeSlotById(id);

        existingTimeSlot.setCourtName(updateTimeSlot.getCourtName());
        existingTimeSlot.setTime(updateTimeSlot.getTime());
        existingTimeSlot.setSek(updateTimeSlot.getSek());
        existingTimeSlot.setEuro(currencyConversionService.convertSEKToEUR(updateTimeSlot.getSek()));
        existingTimeSlot.setAvailable(updateTimeSlot.isAvailable());

        Log4j.logger.info("[Time SLot Updated] with id:" + updateTimeSlot.getTimeSlotId());

        return timeSlotRepository.save(existingTimeSlot);

    }
}
