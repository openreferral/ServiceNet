package org.benetech.servicenet.adapter.icarol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.Collection;
import org.apache.http.Header;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolComplexResponseElement;
import org.benetech.servicenet.adapter.icarol.model.ICarolDataToPersist;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgram;
import org.benetech.servicenet.adapter.icarol.model.ICarolServiceSite;
import org.benetech.servicenet.adapter.icarol.model.ICarolSimpleResponseElement;
import org.benetech.servicenet.adapter.icarol.model.ICarolSite;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.manager.ImportManager;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.benetech.servicenet.util.HttpUtils;
import org.benetech.servicenet.util.ZonedDateTimeDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractICarolDataAdapter extends SingleDataAdapter {

    private static final String AGENCY = "Agency";
    private static final String PROGRAM = "Program";
    private static final String SITE = "Site";
    private static final String SERVICE_SITE = "ServiceSite";
    private static final String TYPE = "type";

    private static final Gson GSON = getICarolGson();

    @Autowired
    private ImportManager importManager;

    @Autowired
    private TransactionSynchronizationService transactionSynchronizationService;

    @Override
    public abstract DataImportReport importData(SingleImportData importData);

    protected DataImportReport importData(SingleImportData importData, String uri) {
        ICarolDataToPersist dataToPersist = gatherMoreDetails(importData, uri);
        RelationManager manager = new RelationManager(importManager);
        DataImportReport dataImportReport = manager.persist(dataToPersist, importData);
        transactionSynchronizationService.registerSynchronizationOfMatchingOrganizations();
        return dataImportReport;
    }

    protected abstract String getApiKey();

    private ICarolDataToPersist gatherMoreDetails(SingleImportData importData, String uri) {
        Header[] headers = HttpUtils.getStandardAuthHeaders(getApiKey());
        return getDataToPersist(importData, headers, uri);
    }

    private ICarolDataToPersist getDataToPersist(SingleImportData importData, Header[] headers, String uri) {
        if (importData.isFileUpload()) {
            return collectDataDetailsFromTheFile(importData.getSingleObjectData());
        }
        Type collectionType = new TypeToken<Collection<ICarolSimpleResponseElement>>() {
        }.getType();
        Collection<ICarolSimpleResponseElement> responseElements = GSON
            .fromJson(importData.getSingleObjectData(), collectionType);
        ICarolComplexResponseElement data = new ICarolComplexResponseElement(responseElements);
        return collectDataDetailsFromTheApi(data, headers, uri);
    }

    private ICarolDataToPersist collectDataDetailsFromTheFile(String file) {
        ICarolDataToPersist dataToPersist = new ICarolDataToPersist();
        JsonArray elements = new Gson().fromJson(file, JsonArray.class);

        for (JsonElement element : elements) {
            JsonObject jsonObject = element.getAsJsonObject();
            String type = jsonObject.get(TYPE).getAsString();
            updateData(dataToPersist, jsonObject, type);
        }

        return dataToPersist;
    }

    private void updateData(ICarolDataToPersist dataToPersist, JsonObject jsonObject, String type) {
        if (type.equals(AGENCY)) {
            dataToPersist.addAgency(GSON.fromJson(jsonObject, ICarolAgency.class));
        }
        if (type.equals(PROGRAM)) {
            dataToPersist.addProgram(GSON.fromJson(jsonObject, ICarolProgram.class));
        }
        if (type.equals(SERVICE_SITE)) {
            dataToPersist.addServiceSite(GSON.fromJson(jsonObject, ICarolServiceSite.class));
        }
        if (type.equals(SITE)) {
            dataToPersist.addSite(GSON.fromJson(jsonObject, ICarolSite.class));
        }
    }

    private ICarolDataToPersist collectDataDetailsFromTheApi(ICarolComplexResponseElement data,
                                                             Header[] headers,
                                                             String uri) {
        ICarolDataToPersist dataToPersist = new ICarolDataToPersist();

        dataToPersist.setPrograms(
            ICarolDataCollector.collectData(data.getProgramBatches(), headers, ICarolProgram.class, uri));
        dataToPersist.setSites(
            ICarolDataCollector.collectData(data.getSiteBatches(), headers, ICarolSite.class, uri));
        dataToPersist.setAgencies(
            ICarolDataCollector.collectData(data.getAgencyBatches(), headers, ICarolAgency.class, uri));
        dataToPersist.setServiceSites(
            ICarolDataCollector.collectData(data.getServiceSiteBatches(), headers, ICarolServiceSite.class, uri));

        return dataToPersist;
    }

    private static Gson getICarolGson() {
        return new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeDeserializer())
            .create();
    }
}
