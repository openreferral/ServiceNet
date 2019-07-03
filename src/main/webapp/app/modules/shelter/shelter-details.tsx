import React, { ComponentClass, StatelessComponent } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Row, Col, Card, CardText, CardBody, CardTitle, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from 'app/entities/shelter/shelter.reducer';
import { withScriptjs, withGoogleMap, GoogleMap, Marker } from 'react-google-maps';
import { GOOGLE_API_KEY } from 'app/config/constants';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';

export interface IShelterDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

const withLatLong = (
  wrappedComponent: string | ComponentClass<any> | StatelessComponent<any>
): string | React.ComponentClass<any> | React.StatelessComponent<any> => wrappedComponent;

const Map = withScriptjs(
  withGoogleMap(
    withLatLong(props => (
      <GoogleMap defaultZoom={8} defaultCenter={{ lat: props.latitude, lng: props.longitude }}>
        {<Marker position={{ lat: props.latitude, lng: props.longitude }} />}
      </GoogleMap>
    ))
  )
);
const mapUrl = 'https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry,drawing,places&key=' + GOOGLE_API_KEY;

export class ShelterDetails extends React.Component<IShelterDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shelterEntity } = this.props;

    return (
      <Row>
        <Col md="6" className="shelter-details">
          <h2>
            {shelterEntity.agencyName}
            {shelterEntity.programName ? <span>: {shelterEntity.programName}</span> : null}
            {shelterEntity.alternateName ? <h4 className="alternate-name">({shelterEntity.alternateName})</h4> : null}
          </h2>
          <Card>
            <CardTitle>
              <Translate contentKey="serviceNetApp.shelter.home.card.eligibilityRequirements" />
            </CardTitle>
            <CardBody>
              <CardText>
                <ul>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.eligibilityDetails" />
                    {': '}
                    {shelterEntity.eligibilityDetails}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.documentsRequired" />
                    {': '}
                    {shelterEntity.documentsRequired}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.languages" />
                    {': '}
                    {shelterEntity.languages
                      ? shelterEntity.languages.map((val, i) => (
                          <span key={val.id}>
                            <a>{val.value}</a>
                            {i === shelterEntity.languages.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.tags" />
                    {': '}
                    {shelterEntity.tags
                      ? shelterEntity.tags.map((val, i) => (
                          <span key={val.id}>
                            <a>{val.value}</a>
                            {i === shelterEntity.tags.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </li>
                </ul>
              </CardText>
            </CardBody>
          </Card>
          <Card>
            <CardTitle>
              <Translate contentKey="serviceNetApp.shelter.home.card.process" />
            </CardTitle>
            <CardBody>
              <CardText>
                <ul>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.applicationProcess" />
                    {': '}
                    {shelterEntity.applicationProcess}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.fees" />
                    {': '}
                    {shelterEntity.fees}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.definedCoverageAreas" />
                    {': '}
                    {shelterEntity.definedCoverageAreas
                      ? shelterEntity.definedCoverageAreas.map((val, i) => (
                          <span key={val.id}>
                            <a>{val.value}</a>
                            {i === shelterEntity.definedCoverageAreas.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.programHours" />
                    {': '}
                    {shelterEntity.programHours}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.holidaySchedule" />
                    {': '}
                    {shelterEntity.holidaySchedule}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.availableBeds" />
                    {': '}
                    {shelterEntity.beds ? shelterEntity.beds.availableBeds : ''}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.waitlist" />
                    {': '}
                    {shelterEntity.beds ? shelterEntity.beds.waitlist : ''}
                  </li>
                </ul>
              </CardText>
            </CardBody>
          </Card>
          <Card>
            <CardTitle>
              <Translate contentKey="serviceNetApp.shelter.home.card.contact" />
            </CardTitle>
            <CardBody>
              <CardText>
                <ul>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.phones" />
                    {': '}
                    {shelterEntity.phones
                      ? shelterEntity.phones.map((val, i) => (
                          <span>
                            {val.number} {val.type},{' '}
                          </span>
                        ))
                      : null}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.address1" />
                    {': '}
                    {shelterEntity.address1}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.address2" />
                    {': '}
                    {shelterEntity.address2}
                  </li>
                  <li>
                    <Translate contentKey="serviceNetApp.shelter.website" />
                    {': '}
                    {shelterEntity.website}
                  </li>
                </ul>
              </CardText>
            </CardBody>
          </Card>
          <Button tag={Link} to="/shelters" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
        </Col>
        <Col md="6">
          {shelterEntity.geocodingResults && (
            <Map
              googleMapURL={mapUrl}
              latitude={shelterEntity.geocodingResults[0].latitude}
              longitude={shelterEntity.geocodingResults[0].longitude}
              loadingElement={<div style={{ height: `100%` }} />}
              containerElement={<div style={{ height: `100%` }} />}
              mapElement={<div style={{ height: `100%` }} />}
            />
          )}
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ shelter }: IRootState) => ({
  shelterEntity: shelter.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShelterDetails);
