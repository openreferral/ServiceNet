import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Row, Col, Card, CardText, CardBody, CardTitle, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from 'app/entities/shelter/shelter.reducer';

export interface IShelterDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShelterDetails extends React.Component<IShelterDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shelterEntity } = this.props;
    return (
      <Row className="shelter-details">
        <Col md="6">
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
