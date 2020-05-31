package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
		meeting = meetingService.addMeeting(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delMeeting(@PathVariable("id") long meetingId) {
		Meeting meeting = meetingService.findById(meetingId);
		if (meeting == null) {
			return new ResponseEntity("Meeting with id: " + meetingId + " does not exist", HttpStatus.NOT_FOUND);
		}
		meeting = meetingService.deleteMeeting(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
    
    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@PathVariable("id") long meetingId, @PathVariable("login") String login) {
		Meeting meeting = meetingService.findById(meetingId);
		Participant participant = participantService.findByLogin(login);

		if (meeting == null || participant == null) {
			return new ResponseEntity("Unable to update. Participant with login " + login + " or meeting with id "
					+ +meetingId + " does not exist", HttpStatus.NOT_FOUND);
		}

		meeting.addParticipant(participant);
		meeting = meetingService.updateMeeting(meeting);
		return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	}
    
    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delParticipant(@PathVariable("id") long meetingId, @PathVariable("login") String login) {
		Meeting meeting = meetingService.findById(meetingId);
		Participant participant = participantService.findByLogin(login);

		if (meeting == null || participant == null) {
			return new ResponseEntity("Unable to update. Participant with login " + login + " or meeting with id "
					+ +meetingId + " does not exist", HttpStatus.NOT_FOUND);
		}

		meeting.removeParticipant(participant);
		meeting = meetingService.updateMeeting(meeting);
		return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	}
}
