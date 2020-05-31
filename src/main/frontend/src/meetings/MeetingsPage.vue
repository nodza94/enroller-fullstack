<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
	  
	  
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: []
            };
        },
		mounted(){
            this.listMeetings();
        },
        methods: {
            addNewMeeting(meeting) {
                
				//this.meetings.push(meeting);
                this.$http.post('meetings', meeting).then(response => this.listMeetings());
            },
            addMeetingParticipant(meeting) {
                //meeting.participants.push(this.username);
				this.$http.post('meetings/' + meeting.id + "/participants/" + this.username).then(response => this.listMeetings());
            },
            removeMeetingParticipant(meeting) {
               // meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
			   this.$http.delete('meetings/' + meeting.id + "/participants/" + this.username).then(response => this.listMeetings());
            },
            deleteMeeting(meeting) {
                this.$http.delete('meetings/' + meeting.id).then(response => this.listMeetings());
            },
			listMeetings() {
				this.$http.get('meetings').then(response => 
				{
					this.meetings = response.body
				});
			}
        }
    }
</script>
