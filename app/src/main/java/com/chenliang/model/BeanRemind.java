package com.chenliang.model;

import java.text.SimpleDateFormat;
import java.util.List;

public class BeanRemind {
    private List<BeanTime> created;
    private List<BeanTime> thirty_min;
    private List<BeanTime> ass_pushed;

    public void setCreated(List<BeanTime> created) {
        this.created = created;
    }

    public List<BeanTime> getCreated() {
        return created;
    }

    public void setThirty_min(List<BeanTime> thirty_min) {
        this.thirty_min = thirty_min;
    }

    public List<BeanTime> getThirty_min() {
        return thirty_min;
    }

    public void setAss_pushed(List<BeanTime> ass_pushed) {
        this.ass_pushed = ass_pushed;
    }

    public List<BeanTime> getAss_pushed() {
        return ass_pushed;
    }

    public class BeanTime {

        private long begin_time;
        private long end_time;
        private long tx_room_id;
        private String type;
        private String clinic_id;

        public void setBegin_time(long begin_time) {
            this.begin_time = begin_time;
        }

        public long getBegin_time() {
            return begin_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setTx_room_id(long tx_room_id) {
            this.tx_room_id = tx_room_id;
        }

        public long getTx_room_id() {
            return tx_room_id;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setClinic_id(String clinic_id) {
            this.clinic_id = clinic_id;
        }

        public String getClinic_id() {
            return clinic_id;
        }

        public String getDateDes() {
            SimpleDateFormat time = new SimpleDateFormat("HH:mm");
            return new SimpleDateFormat("yyyy年MM日").format(begin_time * 1000) + time.format(begin_time * 1000) + "~" + time.format(end_time * 1000);
        }

    }


}
