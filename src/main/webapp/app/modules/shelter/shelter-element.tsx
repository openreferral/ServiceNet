import './shelter-element.scss';

import React from 'react';
import { Row, Col, Card, CardBody, Button } from 'reactstrap';
import { IShelter } from 'app/shared/model/shelter.model';
import { Translate, translate } from 'react-jhipster';
import { Link } from 'react-router-dom';

export interface IShelterElementProps {
  shelter: IShelter;
  editable: boolean;
}

export interface IShelterElementState {
  open: boolean;
}

export class ShelterElement extends React.Component<IShelterElementProps, IShelterElementState> {
  state: IShelterElementState = {
    open: false
  };

  toggle = (event: any) => {
    const open = !this.state.open;
    this.setState({ open });
  };

  render() {
    const { shelter, editable } = this.props;
    return (
      <Row className="shelter-row">
        <Col>
          <a className="alert-link" onClick={this.toggle}>
            <Card className="shelter-card">
              <CardBody className="shelter-card-body">
                <div className="shelter-left-container">
                  <div className="shelter-left-card-text card-text">
                    <div>{shelter.agencyName}</div>
                    <div>{shelter.programName}</div>
                  </div>
                </div>
                <div className="shelter-right-container">
                  <div className="shelter-right-card-info card-text">
                    <div>
                      <Translate contentKey="serviceNetApp.shelter.home.card.bedsAvailable" />:{' '}
                      {shelter.beds && shelter.beds.availableBeds
                        ? shelter.beds.availableBeds
                        : translate('serviceNetApp.shelter.home.card.notAvailable')}
                    </div>
                    <div>
                      <Translate contentKey="serviceNetApp.shelter.home.card.waitlist" />:{' '}
                      {shelter.beds && shelter.beds.waitlist
                        ? shelter.beds.waitlist
                        : translate('serviceNetApp.shelter.home.card.notAvailable')}
                    </div>
                    <div className="shelter-expand-label d-flex align-items-center">
                      {this.state.open ? (
                        <span>
                          <Translate contentKey="serviceNetApp.shelter.home.card.lessInfo" /> ▲
                        </span>
                      ) : (
                        <span>
                          <Translate contentKey="serviceNetApp.shelter.home.card.moreInfo" /> ▼
                        </span>
                      )}
                      {editable ? (
                        <Button tag={Link} to={`shelter/${shelter.id}/edit`} color="primary" className="ml-2 float-right">
                          <span className="d-none d-md-inline">
                            <Translate contentKey="serviceNetApp.shelter.home.card.edit">Edit</Translate>
                          </span>
                        </Button>
                      ) : null}
                    </div>
                  </div>
                </div>
              </CardBody>
            </Card>
          </a>
          {this.state.open && (
            <Card className="shelter-card-expanded">
              <CardBody>
                <div className="card-text">
                  <div className="section">
                    <div className="shelter-info-header">
                      <Translate contentKey="serviceNetApp.shelter.home.card.eligibilityRequirements" />:
                    </div>
                    <div>{shelter.eligibilityDetails}</div>
                    <div>{shelter.documentsRequired}</div>
                  </div>
                  <div className="section">
                    <div className="shelter-info-header">
                      <Translate contentKey="serviceNetApp.shelter.home.card.process" />:
                    </div>
                    <div>{shelter.applicationProcess}</div>
                  </div>
                  <div className="section">
                    <div className="shelter-info-header">
                      <Translate contentKey="serviceNetApp.shelter.home.card.contactInfo" />:
                    </div>
                    {shelter.emails ? shelter.emails.map((val, j) => <div key={j}>{val}</div>) : null}
                    {shelter.phones
                      ? shelter.phones.map((val, j) => (
                          <div key={j}>
                            {val.number} {val.type ? '(' + val.type + ')' : ''}
                          </div>
                        ))
                      : null}
                  </div>
                  <div className="section">
                    <div className="shelter-info-header">
                      <Translate contentKey="serviceNetApp.shelter.home.card.address" />:
                    </div>
                    <div>{shelter.address1}</div>
                    <div>{shelter.address2}</div>
                    <div>{shelter.city}</div>
                    <div>{shelter.zipcode}</div>
                  </div>
                  <div className="section">
                    <Button tag={Link} to={`/shelter/${shelter.id}`} color="info">
                      <span className="d-none d-md-inline">
                        <Translate contentKey="serviceNetApp.shelter.home.card.moreInfo">More info</Translate>
                      </span>
                    </Button>
                    {editable ? (
                      <Button tag={Link} to={`shelter/${shelter.id}/edit`} color="primary" className="ml-1">
                        <span className="d-none d-md-inline">
                          <Translate contentKey="serviceNetApp.shelter.home.card.edit">Edit</Translate>
                        </span>
                      </Button>
                    ) : null}
                  </div>
                </div>
              </CardBody>
            </Card>
          )}
        </Col>
      </Row>
    );
  }
}
export default ShelterElement;
