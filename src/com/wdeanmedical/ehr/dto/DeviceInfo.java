 
package com.wdeanmedical.ehr.dto;

import com.wdeanmedical.ehr.entity.dell.BPT;
import com.wdeanmedical.ehr.entity.dell.GlucoseT;
import com.wdeanmedical.ehr.entity.dell.IOTActivityT;
import com.wdeanmedical.ehr.entity.dell.PulseT;
import com.wdeanmedical.ehr.entity.dell.WeightscaleT;

public class DeviceInfo {

  public BPT bp = new BPT();
  public PulseT pulse = new PulseT();
  public GlucoseT glucose = new GlucoseT();
  public WeightscaleT weightscale = new WeightscaleT();
  public IOTActivityT activity = new IOTActivityT();
  public String phynotes = "";

}
