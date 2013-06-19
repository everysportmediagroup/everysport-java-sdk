package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.Sport;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public interface SportRequest extends Listable<SportRequest>
{

    public SportRequest sport(Object ... sportIds);

    public EverysportResponse<List<Sport>> list() throws IOException;
}
