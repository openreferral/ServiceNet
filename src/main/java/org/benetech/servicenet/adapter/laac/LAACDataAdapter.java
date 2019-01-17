package org.benetech.servicenet.adapter.laac;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.laac.model.LAACData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.type.ListType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("LAACDataAdapter")
public class LAACDataAdapter extends SingleDataAdapter {

    @Override
    public DataImportReport importData(SingleImportData data) {
        List<LAACData> entities = new Gson().fromJson(data.getSingleObjectData(), new ListType<>(LAACData.class));

        return data.getReport();
    }
}
